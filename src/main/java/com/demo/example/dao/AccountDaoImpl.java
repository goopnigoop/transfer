package com.demo.example.dao;

import com.demo.example.entities.Account;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Account dao.
 */
public class AccountDaoImpl extends AbstractDao<Account, UUID> {

    public List getAccountByParameters(String email, String account, String start, String end) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Criteria criteria = currentSession.createCriteria(Account.class);
        if (Objects.nonNull(email)) {
            criteria.add(Restrictions.eq("email", email));
        }
        if (Objects.nonNull(account)) {
            criteria.add(Restrictions.eq("accountName", account));
        }
        if (Objects.nonNull(start)) {
            Date startDate = dateFormat.parse(start);
            criteria.add(Restrictions.ge("created", startDate));
        }
        if (Objects.nonNull(end)) {
            Date endDate = dateFormat.parse(end);
            criteria.add(Restrictions.le("created", endDate));
        }
        return criteria.list();

    }
}
