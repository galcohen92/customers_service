package CustPack.service;

/**
 * The business logic of the Customer -
 */

import CustPack.dao.CustomerDao;
import CustPack.entity.Customer;
import CustPack.util.LoggerWritter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomerService {


    @Autowired
    @Qualifier("mongoData")
    private CustomerDao customerDao;

    public Customer getCustomerById(String custId){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);

        return this.customerDao.getCustomerById(custId);
    }

    public Collection<Customer> getAllCustomers() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.Write(this.getClass(), methodName, "Call");

        return this.customerDao.getAllCustomers();
    }

    public boolean deleteCustomerById(String custId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);

        Customer custTemp = getCustomerById(custId);

        if(custTemp != null) {
            this.customerDao.deleteCustomerById(custId);
        }

        return(custTemp != null);
    }

    public boolean updateCustomer(Customer cust){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", cust.getId());

        String custId = cust.getId();
        Customer custTemp = this.customerDao.getCustomerById(custId);

        if(custTemp != null) {
            this.customerDao.updateCustomer(cust);
        }

        return(custTemp != null);
    }

    public void createCustomer(Customer cust) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", cust.getId());

        customerDao.createCustomer(cust);

    }
}

