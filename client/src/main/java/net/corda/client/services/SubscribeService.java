package net.corda.client.services;

import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.DataFeed;
import net.corda.core.node.services.Vault;
import net.corda.states.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.List;


@Component
public class SubscribeService {

    private static final Logger logger = LoggerFactory.getLogger(SubscribeService.class);

    @Autowired
    private List<CordaRPCOps> cordaRPCOps;


    public void subscribe() {

        /*get the client handle which has the start method
        Secure SSL connection can be established with the server if specified by the client.
        This can be configured by specifying the truststore path containing the RPC SSL certificate in the while creating CordaRPCClient instance.*/
        cordaRPCOps.forEach(proxy -> {

            //hit the node to get snapshot and observable for IOUState
            DataFeed<Vault.Page<Asset>, Vault.Update<Asset>> dataFeed = proxy.vaultTrack(Asset.class);

            //this gives a snapshot of IOUState as of now. so if there are 11 IOUState as of now, this will return 11 IOUState objects
            Vault.Page<Asset> snapshot = dataFeed.getSnapshot();

            //this returns an observable on IOUState
            Observable<Vault.Update<Asset>> updates = dataFeed.getUpdates();

            // call a method for each IOUState
            snapshot.getStates().forEach(SubscribeService::actionToPerform);

            //perform certain action for each update to IOUState
            updates.subscribe(update -> update.getProduced().forEach(SubscribeService::actionToPerform));

        });

    }


    /**
     * Perform certain action because of any update to Observable IOUState
     *
     * @param state
     */
    private static void actionToPerform(StateAndRef<Asset> state) {
        logger.info(">>>>subscribe{}", state.getState().getData());
    }
}
