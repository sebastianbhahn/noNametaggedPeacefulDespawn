package no.named.despawn;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NoDespawn.MODID)
public class NoDespawn
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "no_despawn";

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public NoDespawn(IEventBus modEventBus)
    {

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);


    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

}
