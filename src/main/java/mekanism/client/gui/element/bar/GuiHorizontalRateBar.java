package mekanism.client.gui.element.bar;

import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.bar.GuiBar.IBarInfoHandler;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.resources.ResourceLocation;

public class GuiHorizontalRateBar extends GuiBar<IBarInfoHandler> {

    private static final ResourceLocation RATE_BAR = MekanismUtils.getResource(ResourceType.GUI_BAR, "horizontal_rate.png");
    private static final int texWidth = 78;
    private static final int texHeight = 8;

    public GuiHorizontalRateBar(IGuiWrapper gui, IBarInfoHandler handler, int x, int y) {
        super(RATE_BAR, gui, handler, x, y, texWidth, texHeight, true);
    }

    @Override
    protected void renderBarOverlay(PoseStack matrix, int mouseX, int mouseY, float partialTicks, double handlerLevel) {
        int displayInt = (int) (handlerLevel * texWidth);
        if (displayInt > 0) {
            blit(matrix, x + 1, y + 1, 0, 0, displayInt, texHeight, texWidth, texHeight);
        }
    }
}