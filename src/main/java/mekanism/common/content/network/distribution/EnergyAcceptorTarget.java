package mekanism.common.content.network.distribution;

import java.util.Collection;
import mekanism.api.Action;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.math.FloatingLong;
import mekanism.common.lib.distribution.SplitInfo;
import mekanism.common.lib.distribution.Target;

public class EnergyAcceptorTarget extends Target<IStrictEnergyHandler, FloatingLong, FloatingLong> {

    public EnergyAcceptorTarget() {
    }

    public EnergyAcceptorTarget(Collection<IStrictEnergyHandler> allHandlers) {
        super(allHandlers);
    }

    public EnergyAcceptorTarget(int expectedSize) {
        super(expectedSize);
    }

    @Override
    protected void acceptAmount(IStrictEnergyHandler handler, SplitInfo<FloatingLong> splitInfo, FloatingLong amount) {
        splitInfo.send(amount.subtract(handler.insertEnergy(amount, Action.EXECUTE)));
    }

    @Override
    protected FloatingLong simulate(IStrictEnergyHandler handler, FloatingLong energyToSend) {
        return energyToSend.subtract(handler.insertEnergy(energyToSend, Action.SIMULATE));
    }
}