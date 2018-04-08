package com.moditraders.services;

import com.moditraders.models.Invoice;
import com.moditraders.repositories.CreditNoteRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Hitesh Modi on 08-04-2018.
 */
@Service("creditNoteService")
public class CreditNoteService implements ICreditService {

    @Resource(name = "creditNoteRepo")
    private CreditNoteRepository creditNoteRepo;

    @Override
    public long saveCreditNote(Invoice invoice) {
        return 0;
    }
}
