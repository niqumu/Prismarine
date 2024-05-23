package app.prismarine.server.player;

import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class PrismarinePlayerTextures implements PlayerTextures {

	private URL skin;

	private SkinModel skinModel = SkinModel.CLASSIC;

	private URL cape;

	private long timestamp;

	private boolean signed;

	/**
	 * Checks if the profile stores no textures.
	 *
	 * @return <code>true</code> if the profile stores no textures
	 */
	@Override
	public boolean isEmpty() {
		return this.skin != null && this.cape != null;
	}

	/**
	 * Clears the textures.
	 */
	@Override
	public void clear() {
		this.skin = null;
		this.cape = null;
	}

	/**
	 * Gets the URL that points to the player's skin.
	 *
	 * @return the URL of the player's skin, or <code>null</code> if not set
	 */
	@Override @Nullable
	public URL getSkin() {
		return this.skin;
	}

	/**
	 * Sets the player's skin to the specified URL, and the skin model to
	 * {@link SkinModel#CLASSIC}.
	 * <p>
	 * The URL <b>must</b> point to the Minecraft texture server. Example URL:
	 * <pre>
	 * http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac
	 * </pre>
	 *
	 * @param skinUrl the URL of the player's skin, or <code>null</code> to
	 * unset it
	 */
	@Override
	public void setSkin(@Nullable URL skinUrl) {
		this.skin = skinUrl;
		this.skinModel = SkinModel.CLASSIC;
	}

	/**
	 * Sets the player's skin and {@link SkinModel}.
	 * <p>
	 * The URL <b>must</b> point to the Minecraft texture server. Example URL:
	 * <pre>
	 * http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac
	 * </pre>
	 * <p>
	 * A skin model of <code>null</code> results in {@link SkinModel#CLASSIC} to
	 * be used.
	 *
	 * @param skinUrl the URL of the player's skin, or <code>null</code> to
	 * unset it
	 * @param skinModel the skin model, ignored if the skin URL is
	 * <code>null</code>
	 */
	@Override
	public void setSkin(@Nullable URL skinUrl, @Nullable SkinModel skinModel) {

		// If a skin url was provided, ensure its validity
		if (skinUrl != null) {
			if (!skinUrl.toString().startsWith("http://textures.minecraft.net") &&
					!skinUrl.toString().startsWith("https://textures.minecraft.net")) {
				throw new IllegalArgumentException("URL must point to the texture server!");
			}
		}

		this.skin = skinUrl;
		this.skinModel = skinModel;
	}

	/**
	 * Gets the model of the player's skin.
	 * <p>
	 * This returns {@link SkinModel#CLASSIC} if no skin is set.
	 *
	 * @return the model of the player's skin
	 */
	@Override @NotNull
	public SkinModel getSkinModel() {
		if (this.skin == null || this.skinModel == null) {
			return SkinModel.CLASSIC;
		}

		return this.skinModel;
	}

	/**
	 * Gets the URL that points to the player's cape.
	 *
	 * @return the URL of the player's cape, or <code>null</code> if not set
	 */
	@Override @Nullable
	public URL getCape() {
		return this.cape;
	}

	/**
	 * Sets the URL that points to the player's cape.
	 * <p>
	 * The URL <b>must</b> point to the Minecraft texture server. Example URL:
	 * <pre>
	 * http://textures.minecraft.net/texture/2340c0e03dd24a11b15a8b33c2a7e9e32abb2051b2481d0ba7defd635ca7a933
	 * </pre>
	 *
	 * @param capeUrl the URL of the player's cape, or <code>null</code> to
	 * unset it
	 */
	@Override
	public void setCape(@Nullable URL capeUrl) {

		// If a cape url was provided, ensure its validity
		if (capeUrl != null) {
			if (!capeUrl.toString().startsWith("http://textures.minecraft.net") &&
				!capeUrl.toString().startsWith("https://textures.minecraft.net")) {
				throw new IllegalArgumentException("URL must point to the texture server!");
			}
		}

		this.cape = capeUrl;
	}

	/**
	 * Gets the timestamp at which the profile was last updated.
	 *
	 * @return the timestamp, or <code>0</code> if unknown
	 */
	@Override
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Checks if the textures are signed and the signature is valid.
	 *
	 * @return <code>true</code> if the textures are signed and the signature is
	 * valid
	 */
	@Override
	public boolean isSigned() {
		return this.signed;
	}
}
