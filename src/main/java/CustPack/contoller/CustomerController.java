package CustPack.contoller;

import CustPack.entity.Customer;
import CustPack.service.CustomerService;
import CustPack.util.LoggerWritter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Controller for customer service, responsible of the api http methods
 */

@RestController
@RequestMapping("/customers")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    //For debug purpose - unmark
//    @RequestMapping(method = RequestMethod.GET)
//    public  ResponseEntity<Collection<Customer>> getAllCustomers(){
//        //Log the method calling
//        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//        LoggerWritter.Write(this.getClass(), methodName, "Call");
//
//        Collection<Customer> customers = customerService.getAllCustomers();
//        if(customers.isEmpty()){
//            LoggerWritter.Write(this.getClass(), methodName, "No Content");
//            return new ResponseEntity<Collection<Customer>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
//        }
//
//        LoggerWritter.Write(this.getClass(), methodName, "Ok");
//        return new ResponseEntity<Collection<Customer>>(customers, HttpStatus.OK);
//    }

    @RequestMapping(value = "/{custId}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomerById(@PathVariable("custId") String custId){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);

        Customer customer =  customerService.getCustomerById(custId);
        if (customer == null) {
            LoggerWritter.WriteWithId(this.getClass(), methodName, "Not found", custId);
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }

        LoggerWritter.WriteWithId(this.getClass(), methodName, "Ok", custId);
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }


    @RequestMapping(value = "/{custId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("custId") String custId){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);


        boolean isFound = customerService.deleteCustomerById(custId);

        if (!isFound) {
            LoggerWritter.WriteWithId(this.getClass(), methodName, "Not found ", custId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        LoggerWritter.WriteWithId(this.getClass(), methodName, "Ok", custId);
        return new ResponseEntity(HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCustomer(@Valid @RequestBody Customer customer){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String custId = customer.getId();
        LoggerWritter.WriteWithId(this.getClass(), methodName, "Call", custId);
        boolean isFound =  customerService.updateCustomer(customer);

        if (!isFound) {
            LoggerWritter.WriteWithId(this.getClass(), methodName, "No Content", custId);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        LoggerWritter.WriteWithId(this.getClass(), methodName, "Ok", custId);
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LoggerWritter.Write(this.getClass(), methodName, "Call");

        customerService.createCustomer(customer);


        LoggerWritter.WriteWithId(this.getClass(), methodName, "Create", customer.getId());
        return new ResponseEntity(customer, HttpStatus.CREATED);


    }

}