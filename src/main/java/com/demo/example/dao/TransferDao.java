package com.demo.example.dao;

import com.demo.example.entities.Transfer;
import org.hibernate.Query;

import java.util.List;
import java.util.UUID;

/**
 * The type Transfer dao.
 */
public class TransferDao extends AbstractDao<Transfer, UUID> {

    public List<Transfer> findByAccountFrom(UUID uuid) {
        Query employeeTaskQuery = currentSession.createQuery(
                "from Transfer u where accountfrom=:accountFrom");
        employeeTaskQuery.setParameter("accountFrom", uuid);
        return employeeTaskQuery.list();
    }

    public List<Transfer> findByAccountTo(UUID uuid) {
        Query employeeTaskQuery = currentSession.createQuery(
                "from Transfer u where accountto=:accountTo");
        employeeTaskQuery.setParameter("accountTo", uuid);
        return employeeTaskQuery.list();

    }
}
