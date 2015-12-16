// Generated from Bytecode.g4 by ANTLR 4.4
package com.laneve.bytecode.parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BytecodeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BytecodeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#classfile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassfile(@NotNull BytecodeParser.ClassfileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Monitorenter}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonitorenter(@NotNull BytecodeParser.MonitorenterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code New}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew(@NotNull BytecodeParser.NewContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#packageAndClassName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageAndClassName(@NotNull BytecodeParser.PackageAndClassNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#methodDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclarator(@NotNull BytecodeParser.MethodDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNum(@NotNull BytecodeParser.NumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Store}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStore(@NotNull BytecodeParser.StoreContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(@NotNull BytecodeParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef(@NotNull BytecodeParser.RefContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#identifierExtended}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExtended(@NotNull BytecodeParser.IdentifierExtendedContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#methodModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodModifier(@NotNull BytecodeParser.MethodModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#floatingPointType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatingPointType(@NotNull BytecodeParser.FloatingPointTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Load}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoad(@NotNull BytecodeParser.LoadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Goto}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGoto(@NotNull BytecodeParser.GotoContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#tableEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableEntry(@NotNull BytecodeParser.TableEntryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Const}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst(@NotNull BytecodeParser.ConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotImplemented}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotImplemented(@NotNull BytecodeParser.NotImplementedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Conversion}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversion(@NotNull BytecodeParser.ConversionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#integralType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegralType(@NotNull BytecodeParser.IntegralTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#classDec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDec(@NotNull BytecodeParser.ClassDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#constantAndInfo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantAndInfo(@NotNull BytecodeParser.ConstantAndInfoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Pop}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPop(@NotNull BytecodeParser.PopContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#tableEntries}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableEntries(@NotNull BytecodeParser.TableEntriesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Get}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGet(@NotNull BytecodeParser.GetContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#instructionLine}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstructionLine(@NotNull BytecodeParser.InstructionLineContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Athrow}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAthrow(@NotNull BytecodeParser.AthrowContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#fields}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFields(@NotNull BytecodeParser.FieldsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code If}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(@NotNull BytecodeParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#exceptionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionType(@NotNull BytecodeParser.ExceptionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(@NotNull BytecodeParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#fieldName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldName(@NotNull BytecodeParser.FieldNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#exceptionTypeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionTypeList(@NotNull BytecodeParser.ExceptionTypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#methodBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodBody(@NotNull BytecodeParser.MethodBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#classModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassModifier(@NotNull BytecodeParser.ClassModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#referenceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceType(@NotNull BytecodeParser.ReferenceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#innerClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerClass(@NotNull BytecodeParser.InnerClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#constantPool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantPool(@NotNull BytecodeParser.ConstantPoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Operation}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperation(@NotNull BytecodeParser.OperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Invoke}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvoke(@NotNull BytecodeParser.InvokeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Monitorexit}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonitorexit(@NotNull BytecodeParser.MonitorexitContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult(@NotNull BytecodeParser.ResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#methodHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodHeader(@NotNull BytecodeParser.MethodHeaderContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Iinc}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIinc(@NotNull BytecodeParser.IincContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#fieldModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldModifier(@NotNull BytecodeParser.FieldModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(@NotNull BytecodeParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#formalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameter(@NotNull BytecodeParser.FormalParameterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Dup}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDup(@NotNull BytecodeParser.DupContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Return}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(@NotNull BytecodeParser.ReturnContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#methodName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodName(@NotNull BytecodeParser.MethodNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Put}
	 * labeled alternative in {@link BytecodeParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPut(@NotNull BytecodeParser.PutContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#throws_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrows_(@NotNull BytecodeParser.Throws_Context ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#numericType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericType(@NotNull BytecodeParser.NumericTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link BytecodeParser#formalParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameters(@NotNull BytecodeParser.FormalParametersContext ctx);
}