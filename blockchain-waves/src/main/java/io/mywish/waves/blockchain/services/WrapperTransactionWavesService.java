package io.mywish.waves.blockchain.services;

import com.wavesplatform.wavesj.Transaction;
import com.wavesplatform.wavesj.transactions.SetScriptTransaction;
import io.mywish.blockchain.WrapperOutput;
import io.mywish.blockchain.service.WrapperTransactionService;
import io.mywish.waves.blockchain.model.WrapperTransactionWaves;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class WrapperTransactionWavesService implements WrapperTransactionService<Transaction> {
    @Autowired
    private WrapperOutputWavesService outputBuilder;

    @Override
    public WrapperTransactionWaves build(Transaction transaction) {
        List<String> inputs = Collections.singletonList(transaction.getSenderPublicKey().getAddress());
        List<WrapperOutput> outputs = Collections.singletonList(outputBuilder.build(transaction));

        boolean contractCreation = transaction instanceof SetScriptTransaction;
        String hash = null;
        try {
            hash = transaction.getId().getBase58String();
        } catch (NoSuchMethodError ignored) {
        }

        WrapperTransactionWaves res = new WrapperTransactionWaves(
                hash,
                inputs,
                outputs,
                contractCreation,
                transaction
        );

        if (contractCreation) {
            res.setCreates(transaction.getSenderPublicKey().getAddress());
        }

        return res;
    }
}
