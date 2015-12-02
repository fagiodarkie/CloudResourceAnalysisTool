package tool.model;

public interface IVirtualMachineState extends Datatype {

	public MachineState getState();
	
	public IVirtualMachineState greatestLowerBound(IVirtualMachineState beta);
}
