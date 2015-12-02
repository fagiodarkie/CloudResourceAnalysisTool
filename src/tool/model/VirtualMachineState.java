package tool.model;

public class VirtualMachineState implements IVirtualMachineState {

	private MachineState state;
	
	public VirtualMachineState(MachineState s) {
		state = s;
	}
	
	@Override
	public MachineState getState() {
		return state;
	}

	@Override
	public IVirtualMachineState greatestLowerBound(IVirtualMachineState beta) {
		if (beta.getState() == state) {
			return this;
		}
		if ((state == MachineState.top && beta.getState() == MachineState.bottom)
			|| (state == MachineState.bottom && beta.getState() == MachineState.top)) {
			return new VirtualMachineState(MachineState.partial);
		}
		return new VirtualMachineState(MachineState.unknownReleasing);
	}

}
