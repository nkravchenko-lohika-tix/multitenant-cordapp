package net.corda.client.controllers;

import net.corda.client.common.APIResponse;
import net.corda.client.services.SubscribeService;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.samples.flows.CreateAssetFlow;
import net.corda.states.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api/auction/")
public class AuctionController {
    @Autowired
    private CordaRPCOps partyBProxy;

    @Autowired
    private CordaRPCOps partyCProxy;


    @Autowired
    private SubscribeService subscribeService;

    @PostConstruct
    public void init() {
        this.subscribeService.subscribe();
        this.setupDemoData();
    }


    @GetMapping("asset/list/{party}")
    public APIResponse<List<StateAndRef<Asset>>> getAssetList(@PathVariable String party) {
        try {
            CordaRPCOps activeParty = party.equals("PartyB") ? partyBProxy : partyCProxy;
            List<StateAndRef<Asset>> assetList = activeParty.vaultQuery(Asset.class).getStates();
            return APIResponse.success(assetList);
        } catch (Exception e) {
            return APIResponse.error(e.getMessage());
        }
    }


    /**
     * Create some initial data to play with.
     */
    private void setupDemoData() {
        partyBProxy.startFlowDynamic(CreateAssetFlow.class,
                "The Starry Night",
                "Painted by Vincent van Gogh, this comparatively abstract painting is the signature example of " +
                        "van Gogh's innovative and bold use of thick brushstrokes. The painting's striking blues and " +
                        "yellows and the dreamy, swirling atmosphere have intrigued art lovers for decades.",
                "img/The_Scary_Night.jpg");

        partyCProxy.startFlowDynamic(CreateAssetFlow.class,
                "The Scream",
                "First things first -- \"The Scream\" is not a single work of art. According to a British Museum's blog," +
                        " there are two paintings, two pastels and then an unspecified number of prints. Date back to " +
                        "the the year 1893, this masterpiece is a work of Edvard Munch",
                "img/The_Scream.jpg");

    }

}
