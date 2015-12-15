package com.laneve.deadlock.visitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.antlr.v4.runtime.misc.NotNull;

import com.laneve.bytecode.parser.BytecodeBaseVisitor;
import com.laneve.bytecode.parser.BytecodeParser;
import com.laneve.bytecode.parser.BytecodeParser.ConstContext;
import com.laneve.bytecode.parser.BytecodeParser.FormalParameterContext;
import com.laneve.bytecode.parser.BytecodeParser.MethodDeclarationContext;
import com.laneve.bytecode.parser.BytecodeParser.MethodModifierContext;
import com.laneve.bytecode.parser.BytecodeParser.RefContext;
import com.laneve.bytecode.parser.BytecodeParser.StoreContext;
import com.laneve.bytecode.parser.BytecodeParser.TableEntryContext;
import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.BEBase;
import com.laneve.deadlock.models.BEClassFile;
import com.laneve.deadlock.models.BEConstantAndInfo;
import com.laneve.deadlock.models.BEConstantPool;
import com.laneve.deadlock.models.BEInstructionLine;
import com.laneve.deadlock.models.BEMethodBody;
import com.laneve.deadlock.models.BEMethodDeclaration;
import com.laneve.deadlock.models.BEMethodDeclarator;
import com.laneve.deadlock.models.BEMethodHeader;
import com.laneve.deadlock.models.BETableEntry;
import com.laneve.deadlock.models.instructions.BEAthrow;
import com.laneve.deadlock.models.instructions.BEConst;
import com.laneve.deadlock.models.instructions.BEConversion;
import com.laneve.deadlock.models.instructions.BEDup;
import com.laneve.deadlock.models.instructions.BEGet;
import com.laneve.deadlock.models.instructions.BEGoto;
import com.laneve.deadlock.models.instructions.BEIf;
import com.laneve.deadlock.models.instructions.BEIinc;
import com.laneve.deadlock.models.instructions.BEInvoke;
import com.laneve.deadlock.models.instructions.BELoad;
import com.laneve.deadlock.models.instructions.BEMonitorEnter;
import com.laneve.deadlock.models.instructions.BEMonitorExit;
import com.laneve.deadlock.models.instructions.BENew;
import com.laneve.deadlock.models.instructions.BENotImplemented;
import com.laneve.deadlock.models.instructions.BEOperation;
import com.laneve.deadlock.models.instructions.BEPop;
import com.laneve.deadlock.models.instructions.BEPut;
import com.laneve.deadlock.models.instructions.BEReturn;
import com.laneve.deadlock.models.instructions.BEStore;

public class BytecodeVisitor extends BytecodeBaseVisitor<BEBase> {
	
	
	private long staticCounter=0; //contatore per assegnare nome univoco ai blocchi statici

	
	@Override 
	public BEBase visitClassfile(@NotNull BytecodeParser.ClassfileContext ctx) {		
		String className = ctx.classDec().ilmio.getText();
		BEConstantPool costantPool = (BEConstantPool) visitConstantPool(ctx.constantPool());
		BEClassFile classFile = new BEClassFile(className,costantPool);
		for(MethodDeclarationContext mdc : ctx.methodDeclaration()){
			BEMethodDeclaration methodDec = (BEMethodDeclaration) visitMethodDeclaration(mdc);
			try {
				classFile.addMethod(methodDec);
			} catch (BEException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return classFile;
	}
	
	@Override 
	public BEBase visitConstantPool(@NotNull BytecodeParser.ConstantPoolContext ctx) { 
		BEConstantPool constantPool=new BEConstantPool(); 
		for(TableEntryContext tb : ctx.tableEntries().tableEntry()){
			BETableEntry tableEntry = (BETableEntry) visitTableEntry(tb);
			constantPool.addTableEntry(tableEntry);
		}
		return constantPool;
	}

	@Override
	public BEBase visitTableEntry(@NotNull BytecodeParser.TableEntryContext ctx) { 
		String ref = ctx.ref().getText();
		BEConstantAndInfo costantAndInfo = (BEConstantAndInfo) visitConstantAndInfo(ctx.constantAndInfo());
		BETableEntry tableEntry = new BETableEntry(ref, costantAndInfo);
		return tableEntry; 
	}

	@Override
	public BEBase visitConstantAndInfo(@NotNull BytecodeParser.ConstantAndInfoContext ctx) { 
		ArrayList<String> c = new ArrayList<String>();
		String type = ctx.getChild(0).getText();
		c.add(type);

		if(!type.contentEquals("Utf8")){
			for(RefContext r : ctx.ref()){
				c.add(r.getText());
			}
		}else{
			String utf8 = ctx.getText();
			int n = utf8.indexOf('8');
			String utf8Ref = utf8.subSequence(n+1, utf8.length()).toString();
			c.add(utf8Ref);
		}

		BEConstantAndInfo costantAndInfo = new BEConstantAndInfo(c);
		return costantAndInfo;
	}
	
	@Override 
	public BEBase visitMethodDeclaration(@NotNull BytecodeParser.MethodDeclarationContext ctx) { 
		BEMethodDeclaration methodDeclaration;
		String methodModifier = "";
		BEMethodBody methodBody;
		BEMethodHeader methodHeader;

		for(MethodModifierContext m : ctx.methodModifier()){
			methodModifier = methodModifier + " " + m.getText();
		}
		
		if(ctx.methodHeader()!=null){
			methodHeader = (BEMethodHeader) visitMethodHeader(ctx.methodHeader());
		}
		else{ /* blocco statico (non ha nome metodo e parametri formali quindi gli assegniamo un nome univoco;
				il '?' non puo' essere usato nei nomi di metodi in Java per cui non ci saranno altri metodi con lo stesso nome) */
			methodHeader = new BEMethodHeader("void",new BEMethodDeclarator("staticBlock?"+ staticCounter++,
										new ArrayList<BytecodeParser.FormalParameterContext>()));
		}
		
		methodBody = (BEMethodBody) visitMethodBody(ctx.methodBody());
		try {
			methodBody.setMethodSignature(methodModifier,methodHeader);
		} catch (BEException e) {
			e.printStackTrace();
			System.exit(1);
		}
		methodDeclaration = new BEMethodDeclaration(methodModifier, methodHeader, methodBody);

		return methodDeclaration;
	}

	@Override public BEBase visitMethodHeader(@NotNull BytecodeParser.MethodHeaderContext ctx) { 
		BEMethodHeader methodHeader;
		String result = null;
		BEMethodDeclarator methodDeclarator = (BEMethodDeclarator) visitMethodDeclarator(ctx.methodDeclarator());
		if(ctx.result() == null)
			result = "void";
		else result = ctx.result().getText();

		methodHeader = new BEMethodHeader(result,methodDeclarator);
		return methodHeader;
	}

	@Override public BEBase visitMethodDeclarator(@NotNull BytecodeParser.MethodDeclaratorContext ctx) { 
		BEMethodDeclarator methodDeclarator;
		ArrayList<FormalParameterContext> formalParameters = new ArrayList<BytecodeParser.FormalParameterContext>();
		if(!(ctx.formalParameters() == null)){
			for(FormalParameterContext f : ctx.formalParameters().formalParameter()){
				formalParameters.add(f);
			}
		}
		methodDeclarator = new BEMethodDeclarator(ctx.methodName().packageAndClassName().getText().replace(".", "/"), formalParameters);

		return methodDeclarator; 
	}
	
	@Override public BEBase visitMethodBody(@NotNull BytecodeParser.MethodBodyContext ctx) {
		BEMethodBody methodBody;
		LinkedList<BEInstructionLine> instruction = new LinkedList<BEInstructionLine>();
		
		HashMap<String, Integer> instructionMap = new HashMap<String, Integer>();

		for(int i= 0; i<ctx.instructionLine().size(); i++){
			BEInstructionLine instructionLine = (BEInstructionLine) visitInstructionLine(ctx.instructionLine().get(i));
			instruction.add(instructionLine);
			instructionMap.put(instructionLine.getIndex().substring(0, instructionLine.getIndex().indexOf(":")), i);
		}
		
		methodBody = new BEMethodBody(instruction,instructionMap);
		return methodBody; 
	}

	@Override public BEBase visitInstructionLine(@NotNull BytecodeParser.InstructionLineContext ctx) {
		BEInstructionLine instruction = (BEInstructionLine) visit(ctx.instruction());
		instruction.setIndex(ctx.INDEX() == null ? " ":ctx.INDEX().getText());
		instruction.setNumber(ctx.num() == null ? " ":ctx.num().getText());
		instruction.setNat(ctx.NAT() == null? " ":ctx.NAT().getText());
		instruction.setRef(ctx.ref() == null? " ":ctx.ref().getText());
		return instruction;
	}
	
	@Override public BEBase visitPop(com.laneve.bytecode.parser.BytecodeParser.PopContext ctx) {
		return new BEPop(ctx.getText());
	};
	
	@Override public BEBase visitConversion(com.laneve.bytecode.parser.BytecodeParser.ConversionContext ctx) {
		return new BEConversion(ctx.getText());
	};
	
	@Override public BEBase visitNew(com.laneve.bytecode.parser.BytecodeParser.NewContext ctx) {
		return new BENew(ctx.getText());
	};
	
	@Override public BEBase visitMonitorexit(com.laneve.bytecode.parser.BytecodeParser.MonitorexitContext ctx) {
		return new BEMonitorExit(ctx.getText());
	};
	
	@Override public BEBase visitMonitorenter(com.laneve.bytecode.parser.BytecodeParser.MonitorenterContext ctx) {
		return new BEMonitorEnter(ctx.getText());
	};
	
	@Override public BEBase visitInvoke(com.laneve.bytecode.parser.BytecodeParser.InvokeContext ctx) {
		return new BEInvoke(ctx.getText());
	};
	
	@Override public BEBase visitIf(com.laneve.bytecode.parser.BytecodeParser.IfContext ctx) {
		return new BEIf(ctx.getText());
	};
	
	@Override public BEBase visitOperation(com.laneve.bytecode.parser.BytecodeParser.OperationContext ctx) {
		return new BEOperation(ctx.getText());
	};
	
	@Override public BEBase visitNotImplemented(com.laneve.bytecode.parser.BytecodeParser.NotImplementedContext ctx) {
		return new BENotImplemented(ctx.getText());
	};
	
	@Override public BEBase visitDup(com.laneve.bytecode.parser.BytecodeParser.DupContext ctx) {
		return new BEDup(ctx.getText());
	};
	
	@Override public BEBase visitAthrow(com.laneve.bytecode.parser.BytecodeParser.AthrowContext ctx) {
		return new BEAthrow(ctx.getText());
	};
	
	@Override public BEBase visitReturn(com.laneve.bytecode.parser.BytecodeParser.ReturnContext ctx) {
		return new BEReturn(ctx.getText());
	};
	
	@Override public BEBase visitLoad(com.laneve.bytecode.parser.BytecodeParser.LoadContext ctx) {
		return new BELoad(ctx.getText());
	};
	
	@Override public BEBase visitGet(com.laneve.bytecode.parser.BytecodeParser.GetContext ctx) {
		return new BEGet(ctx.getText());
	};
	
	@Override public BEBase visitPut(com.laneve.bytecode.parser.BytecodeParser.PutContext ctx) {
		return new BEPut(ctx.getText());
	};
	
	@Override public BEBase visitIinc(com.laneve.bytecode.parser.BytecodeParser.IincContext ctx) {
		return new BEIinc(ctx.getText());
	};
	
	@Override public BEBase visitGoto(com.laneve.bytecode.parser.BytecodeParser.GotoContext ctx) {
		return new BEGoto(ctx.getText());
	};
	
	@Override
	public BEBase visitStore(StoreContext ctx) {
		return new BEStore(ctx.getText());
	}
	
	@Override
	public BEBase visitConst(ConstContext ctx) {
		return new BEConst(ctx.getText());
	}


}
