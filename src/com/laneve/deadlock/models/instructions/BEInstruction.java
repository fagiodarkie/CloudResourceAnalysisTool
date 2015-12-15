package com.laneve.deadlock.models.instructions;

import com.laneve.deadlock.exceptions.BEException;
import com.laneve.deadlock.models.Environment;

public interface BEInstruction {
	void updateEnvironment(Environment environment) throws BEException;
}
