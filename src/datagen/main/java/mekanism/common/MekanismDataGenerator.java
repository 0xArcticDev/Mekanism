package mekanism.common;

import com.electronwill.nightconfig.core.CommentedConfig;
import mekanism.client.lang.MekanismLangProvider;
import mekanism.client.model.MekanismItemModelProvider;
import mekanism.client.sound.MekanismSoundProvider;
import mekanism.client.state.MekanismBlockStateProvider;
import mekanism.client.texture.PrideRobitTextureProvider;
import mekanism.common.advancements.MekanismAdvancementProvider;
import mekanism.common.advancements.MekanismCriteriaTriggers;
import mekanism.common.loot.MekanismLootProvider;
import mekanism.common.recipe.impl.MekanismRecipeProvider;
import mekanism.common.tag.MekanismTagProvider;
import mekanism.common.world_modifier.MekanismBiomeModifierProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = Mekanism.MODID, bus = Bus.MOD)
public class MekanismDataGenerator {

    private MekanismDataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        bootstrapConfigs(Mekanism.MODID);
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        //Bootstrap our advancement triggers as common setup doesn't run
        MekanismCriteriaTriggers.init();
        //Client side data generators
        gen.addProvider(event.includeClient(), new MekanismLangProvider(gen));
        gen.addProvider(event.includeClient(), new PrideRobitTextureProvider(gen, existingFileHelper));
        gen.addProvider(event.includeClient(), new MekanismSoundProvider(gen, existingFileHelper));
        //Let the blockstate provider see models generated by the item model provider
        MekanismItemModelProvider itemModelProvider = new MekanismItemModelProvider(gen, existingFileHelper);
        gen.addProvider(event.includeClient(), itemModelProvider);
        gen.addProvider(event.includeClient(), new MekanismBlockStateProvider(gen, itemModelProvider.existingFileHelper));
        //Server side data generators
        gen.addProvider(event.includeServer(), new MekanismTagProvider(gen, existingFileHelper));
        gen.addProvider(event.includeServer(), new MekanismLootProvider(gen));
        gen.addProvider(event.includeServer(), new MekanismBiomeModifierProvider(gen));
        gen.addProvider(event.includeServer(), new MekanismRecipeProvider(gen, existingFileHelper));
        gen.addProvider(event.includeServer(), new MekanismAdvancementProvider(gen, existingFileHelper));
        //TODO - 1.19: Re-enable when ProjectE updates and then disable it in the persisting data providers
        //gen.addProvider(event.includeServer(), new MekanismCustomConversions(gen));
        //TODO - 1.19: Re-enable when CrT updates and then disable it in the persisting data providers
        //gen.addProvider(event.includeServer(), new MekanismCrTExampleProvider(gen, existingFileHelper));
        //Data generator to help with persisting data when porting across MC versions when optional deps aren't updated yet
        gen.addProvider(true, new PersistingDisabledProvidersProvider(gen));
    }

    /**
     * Used to bootstrap configs to their default values so that if we are querying if things exist we don't have issues with it happening to early or in cases we have
     * fake tiles.
     */
    public static void bootstrapConfigs(String modid) {
        ConfigTracker.INSTANCE.configSets().forEach((type, configs) -> {
            for (ModConfig config : configs) {
                if (config.getModId().equals(modid)) {
                    //Similar to how ConfigTracker#loadDefaultServerConfigs works for loading default server configs on the client
                    // except we don't bother firing an event as it is private, and we are already at defaults if we had called earlier,
                    // and we also don't fully initialize the mod config as the spec is what we care about, and we can do so without having
                    // to reflect into package private methods
                    CommentedConfig commentedConfig = CommentedConfig.inMemory();
                    config.getSpec().correct(commentedConfig);
                    config.getSpec().acceptConfig(commentedConfig);
                }
            }
        });
    }
}