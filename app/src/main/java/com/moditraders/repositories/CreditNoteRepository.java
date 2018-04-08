package com.moditraders.repositories;

import com.moditraders.entities.CreditNote;
import com.moditraders.entities.Invoicedetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository("creditNoteRepo")
public interface CreditNoteRepository extends CrudRepository<CreditNote, String>{

}
