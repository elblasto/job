/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ph.dal.hibernatedao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import ph.client.commons.exceptions.DALException;
import ph.dal.dao.DistrDAO;
import ph.dal.entities.documents.DistrInvoice;
import ph.dal.entities.documents.Enterprise;
import ph.dal.entities.documents.ExpOrder;
import ph.dal.entities.documents.Fail;
import ph.dal.entities.documents.Order;
import ph.dal.entities.documents.TransporterCars;
import ph.dal.entities.documents.TransporterDrivers;
import ph.dal.entities.documents.Transporters;
import ph.dal.entities.references.Client;
import ph.dal.entities.references.ClientDistributAdress;
import ph.dal.entities.staff.Employee;
import ph.dal.entities.staff.Position;

/**
 * Реализация DistrDAO на основе Hibernate
 *
 */
public class DistrDAOHibImpl extends HibernateDAOImpl implements DistrDAO {

    public DistrDAOHibImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void copyDistrDetails(Integer sourceOrderId, Integer destOrderId)  throws DALException {
        Session session = null;
        Transaction tr = null;
        try{
            session = openSession();
            Query query = session.createSQLQuery("{call p_doc_Orders_copyDistrDetails(?, ?)}");
            query.setInteger(0, sourceOrderId);
            query.setInteger(1, destOrderId);
            tr = session.beginTransaction();
            query.executeUpdate();
            tr.commit();
        } catch (Exception ex){
            if (tr!=null)
                tr.rollback();
            throw new DALException(ex);
        } finally {
            closeSession(session);
        }       
    }

    public List<Enterprise> getEnterprises(){
        String query = "select e from Enterprise e " +
                      "order by e.name ";
        return queryList(query);
    }

    public void saveOrder(Order order)  throws DALException{
        saveOrUpdate(order);
    }

    public void saveInvoice(DistrInvoice invoice)  throws DALException{
        saveOrUpdate(invoice);
    }

    public List<Client> getClientList(boolean receiversOnly)  throws DALException{
       String query = "select c from Client c " +
                      "where c.isProductionReceiver = ? " +
                      "order by c.name ";
        return queryList(query, receiversOnly ? 1 : 0);
    }
    

   public Position getEmpPosition(Employee Emp)  throws DALException{
       String queryString = "Select e.position from EmpPosition e " +
                      "where e.employee = :i and e.rate=1 ";
      Session session = null;
        Transaction tr = null;
        try{
            session = openSession();
            tr = session.beginTransaction();

            Query query = session.createQuery(queryString);
            query.setParameter("i", Emp);
            Position result = (Position) query.uniqueResult();  
            tr.commit();
            return result;
        } catch (HibernateException ex){
            if (tr!=null)
                tr.rollback();
            throw ex;
        } finally {
           closeSession(session);
        }    
   }        
    
    public BigDecimal getDisrtinvoiceWeight(Integer Dstr)  throws  DALException{
       String queryString = "Select sum(dbo.kolpechlist(null,p.Id,null,null,null,0)*" +
                   "dbo.ploshadmater(null,null,null,m.Id,null,0)*" +
                   "dbo.plotnbumagi(null,null,null,m.Id,null,0)/1000) as WeightOneDoc " + 
                   " from OrderDistrInvoices o" +
                " inner join Documents d " +         
                   "on o.OrderId = d.Id" +
                " inner join Parts p" +
                   " on p.DocumentId=d.Id" +
                " inner join DocOperations dop" +
                   " on dop.PartId=p.Id and" +
                " dop.OperationId = dbo.const_operPrint()" +
                " inner join DocMaterials dm" +
                   " on dm.DocOperId = dop.Id " +
                " inner join Materials m" +
                   " on dm.MaterialId=m.Id and" +
                      " m.IsPaper = 1" +
                   " where o.Id= :i ";
                      
      //return (Double) queryUniqueResult(queryString, Dstr);
   //}
      Session session = null;
        Transaction tr = null;
        try{
            session = openSession();
            tr = session.beginTransaction();

            Query query = session.createSQLQuery(queryString);
            query.setParameter("i", Dstr);
            BigDecimal result = (BigDecimal) query.uniqueResult();  
            tr.commit();
            return result;
        } catch (HibernateException ex){
            if (tr!=null)
                tr.rollback();
            throw ex;
        } finally {
            closeSession(session);
        }    
   }
    
    /**заменить процесс получения результата запроса на процедуру из HibernateDAOImpl.java
    */
    public List<ClientDistributAdress> getClientDistributAdressesList(Client Clnt)  throws DALException{
       String queryString = "select c from ClientDistributAdress c " +
                      "where c.client= :i " +
                      "order by c.Adress ";
       return queryList(queryString,Clnt);
     }            
   
    public List<Transporters> getTransportersList()  throws DALException{
       String query = "select t from Transporters t " +
                      "order by t.name ";
        return queryList(query);
        }
 
    public List<TransporterCars> getTransporterCarList(Transporters trnsp)  throws DALException{
       String queryString = "select tc from TransporterCars tc " +
                      "where tc.transporter= :i " +
                      "order by tc.name ";
        //return queryList(queryString,trnsp);        }
        Session session = null;
        Transaction tr = null;
        try{
            session = openSession();
            tr = session.beginTransaction();
        
            Query query = session.createQuery(queryString);
            query.setParameter("i", trnsp);
            List result = query.list();  
            tr.commit();
            
            return result;              
        } catch (HibernateException ex) {
            if (tr!=null)
                tr.rollback();
            throw (ex);
        } finally {
            closeSession(session);
          } 
        }
    public List<TransporterDrivers> getTransporterDriverList(Transporters trnsp)  throws DALException{
       String queryString = "select td from TransporterDrivers td " +
                      "where td.transporter= :i " +
                      "order by td.FIO ";
        return queryList(queryString,trnsp);
        }
           
    public Order getOrder(Integer orderId) throws DALException{
        Session session = null;
        try{
            session = openSession();
            Criteria crit = session.createCriteria(Order.class);
            crit.add(Restrictions.idEq(orderId));
            crit.setFetchMode("distrItems", FetchMode.JOIN);
            crit.setFetchMode("product", FetchMode.JOIN);
            crit.setFetchMode("client", FetchMode.JOIN);
            crit.setFetchMode("enterprise", FetchMode.JOIN);
            Order order = (Order) crit.uniqueResult();
            order.getDistrInvoices().size();
            return order;
        } catch(HibernateException ex){
            throw new DALException(ex);
        }
        finally{
            closeSession(session);
        }
    }


    public List<Order> getOrders(Date startDate, Date endDate) throws DALException{
        String query = "select distinct o from Order o " +
                       "inner join fetch  o.product " +
                       "inner join fetch  o.client " +
                       "left join fetch o.enterprise " +
                       "left join fetch o.distrInvoices "+
                       "where (o.readyDate >= ?) " +
                       "      and (o.readyDate < ?) " +
                       "order by o.readyDate ";

        return queryList(query, startDate, endDate);
    }

    public List<ExpOrder> getExpOrders(Date startDate, Date endDate) throws DALException{
        String qs = "select    OrderId, " +
                "              OrderNum, " +
                "              IssueNum, " +
                "              ClientName, " +
                "              ProductName, " +
                "              Description, " +
                "              Circulation, " +
                "              PlanReadyDate, " +
                "              FactReadyDate, " +
                "              ExpeditionComplete, " +
                "              InvoiceCount, " +
                "              EnterpriseName "+
                "from dbo.v_OrdersForExpedition " +
                "where (PlanReadyDate>= :startDate) and (PlanReadyDate<:endDate) " +
                "order by PlanReadyDate ";
        Session session = openSession();
        SQLQuery query = session.createSQLQuery(qs);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<Object[]> queryres = query.list();
        closeSession(session);
        List<ExpOrder> res = new ArrayList<ExpOrder>();
        for (Object[] obj : queryres){
            ExpOrder o = new ExpOrder();
            o.setOrderId((Integer)obj[0]);
            o.setOrderNum((Integer)obj[1]);
            o.setIssueNum((Integer)obj[2]);
            o.setClientName((String)obj[3]);
            o.setProductName((String)obj[4]);
            o.setDescription((String)obj[5]);
            o.setCirculation((Integer)obj[6]);
            o.setPlanReadyDate((Date)obj[7]);
            o.setFactReadyDate((Date)obj[8]);
            o.setExpeditionComplete((Integer)obj[9]);
            o.setInvoiceCount((Integer)obj[10]);
            o.setEnterpriseName((String)obj[11]);
            res.add(o);
        }
        return res;
    }

    public void setExpeditionComplete(Integer orderId, int expeditionComplete) throws DALException{
        String updateQuery = "update Orders o " +
                             "set o.ExpeditionComplete = :complete " +
                             "where o.DocumentId = :orderId";
        Session session = null;
        Transaction tr = null;
        try{
            session = openSession();
            tr = session.beginTransaction();
                Query query = session.createQuery(updateQuery);                
                query.setInteger("complete", expeditionComplete);
                query.setInteger("orderId", orderId);
                query.executeUpdate();
            tr.commit();
        } catch (Exception ex){
            if (tr!=null)
                tr.rollback();
            throw new DALException(ex);
        } finally {
            closeSession(session);
        }            
    }

    public List<Fail> getDistrFails(Order order)  throws DALException{
        String query = "select f from Fail f " +
                       "where (f.order = ?) " +
                       "      and (f.typeId = ?)";
        return queryList(query, order, Fail.FT_DISTR);
    }

}
