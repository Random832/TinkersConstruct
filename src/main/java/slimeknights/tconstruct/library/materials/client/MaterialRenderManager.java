package slimeknights.tconstruct.library.materials.client;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.materials.IMaterial;

import java.util.Map;

/**
 * This class takes care of loading all the rendering information for materials
 */
public class MaterialRenderManager extends JsonReloadListener {

  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
  private static final Logger LOGGER = LogManager.getLogger();

  private Map<ResourceLocation, IMaterial> materials = ImmutableMap.of();

  public MaterialRenderManager() {
    super(GSON, "tic_materials");
  }

  @Override
  protected void apply(Map<ResourceLocation,JsonElement> splashList, IResourceManager resourceManagerIn, IProfiler profilerIn) {
    // todo: actually keep the data
    splashList.forEach((resourceLocation, json) -> LOGGER.info("{}: {}", resourceLocation, json));
  }
}
