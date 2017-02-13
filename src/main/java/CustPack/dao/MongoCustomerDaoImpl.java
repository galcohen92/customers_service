package CustPack.dao;

import CustPack.entity.Customer;
import CustPack.util.LoggerWritter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Implement dao method of customers over mongodb
 */

@Repository
@Qualifier("mongoData")
public class MongoCustomerDaoImpl implements CustomerDao {

    @Autowired
    private MongoCustomerRepoistory repository;


    @Override
    public Customer getCustomerById(String custId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);
        return repository.findOne(custId);
    }

    @Override
    public void deleteCustomerById(String custId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);

        repository.delete(custId);
    }

    @Override
    public void createCustomer(Customer cust) {
        cust.setId((new ObjectId()).toString());//can instead to depend on counter id table
        String custId = cust.getId();

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);

        repository.insert(cust);

    }

    @Override
    public void updateCustomer(Customer cust) {
        String custId = cust.getId();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);

        repository.save(cust);

    }

    @Override
    public Collection<Customer> getAllCustomers() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.Write(this.getClass(), methodName, "Call");

        return repository.findAll();
    }

    public void deleteAllCustomers() {
        repository.deleteAll();
    }
}
