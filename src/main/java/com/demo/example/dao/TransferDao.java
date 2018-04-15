package com.demo.example.dao;

import com.demo.example.entities.Transfer;
import org.hibernate.Query;

import java.util.List;
import java.util.UUID;

/**
 * The type Transfer dao.
 */
public class TransferDao extends AbstractDao<Transfer, UUID> {

    /**
     * Find by account from list.
     *
     * @param uuid the uuid
     * @return the list
     */
    public List findByAccountFrom(UUID uuid) {
        Query employeeTaskQuery = currentSession.createQuery(
                "from Transfer u where account_from=:accountFrom");
        employeeTaskQuery.setParameter("accountFrom", uuid);
        return employeeTaskQuery.list();
    }

    /**
     * Find by account to list.
     *
     * @param uuid the uuid
     * @return the list
     */
    public List findByAccountTo(UUID uuid) {
        Query employeeTaskQuery = currentSession.createQuery(
                "from Transfer u where account_to=:accountTo");
        employeeTaskQuery.setParameter("accountTo", uuid);
        return employeeTaskQuery.list();

    }
}
