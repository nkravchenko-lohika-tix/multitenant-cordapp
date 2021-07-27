package net.corda.states;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.CommandAndState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.OwnableState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.serialization.ConstructorForDeserialization;
import net.corda.contracts.AssetContract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * An ownable state to represent an asset that could be put on auction.
 */
@BelongsToContract(AssetContract.class)
public class Asset implements OwnableState, LinearState {

    private UniqueIdentifier linearId;
    private String title;
    private String description;
    private String imageUrl;
    private AbstractParty owner;

    @ConstructorForDeserialization
    public Asset() {
    }

    public Asset(UniqueIdentifier linearId, String title, String description, String imageUrl, AbstractParty owner) {
        this.linearId = linearId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.owner = owner;
    }

    public void setLinearId(UniqueIdentifier linearId) {
        this.linearId = linearId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setOwner(AbstractParty owner) {
        this.owner = owner;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner);
    }

    @NotNull
    @Override
    public AbstractParty getOwner() {
        return owner;
    }

    /**
     * This method should be called to retrieve an ownership transfer command and the updated state with the new owner
     * passed as a parameter to the method.
     *
     * @param newOwner of the asset
     * @return A CommandAndState object encapsulating the command and the new state with the changed owner, to be used
     * in the ownership transfer transaction.
     */
    @NotNull
    @Override
    public CommandAndState withNewOwner(@NotNull AbstractParty newOwner) {
        return new CommandAndState(new AssetContract.Commands.TransferAsset(),
                new Asset(this.getLinearId(), this.getTitle(), this.getDescription(), this.getImageUrl(), newOwner));
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return linearId;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "linearId=" + linearId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", owner=" + owner +
                '}';
    }
}
