package me.dreamdevs.slender.api.game.party;

import me.dreamdevs.slender.api.database.IGamePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface IParty {

	/**
	 * This returns online leader party.
	 */

	Player getPartyLeader();

	/**
	 * This returns all party members.
	 */

	List<IGamePlayer> getMembers();

	/**
	 * Method to set some party settings, add some in the future.
	 */

	void setPartySetting(PartySettings partySettings, Object value);

	/**
	 * Returns the value of setting.
	 */

	Object getPartySetting(PartySettings partySettings);

}