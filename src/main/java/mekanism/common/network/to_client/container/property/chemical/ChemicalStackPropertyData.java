package mekanism.common.network.to_client.container.property.chemical;

import javax.annotation.Nonnull;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.ChemicalUtils;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.network.to_client.container.property.PropertyData;
import mekanism.common.network.to_client.container.property.PropertyType;
import net.minecraft.network.FriendlyByteBuf;

public abstract class ChemicalStackPropertyData<STACK extends ChemicalStack<?>> extends PropertyData {

    @Nonnull
    protected final STACK value;

    public ChemicalStackPropertyData(PropertyType propertyType, short property, @Nonnull STACK value) {
        super(propertyType, property);
        this.value = value;
    }

    @Override
    public void handleWindowProperty(MekanismContainer container) {
        container.handleWindowProperty(getProperty(), value);
    }

    @Override
    public void writeToPacket(FriendlyByteBuf buffer) {
        super.writeToPacket(buffer);
        ChemicalUtils.writeChemicalStack(buffer, value);
    }
}