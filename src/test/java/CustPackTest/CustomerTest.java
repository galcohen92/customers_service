package CustPackTest;//package CustPackTest;


import CustPack.Main;
import CustPack.dao.MongoCustomerDaoImpl;
import CustPack.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.maven.plugins.annotations.Component;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
//import org.springframework.http.MediaType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.HttpsURLConnection;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * Test drive Documenting RESTful APIs with Spring REST Docs
 * 1. The tests genreate snipptes that inject as example in the asciidoctor file
 * 2. call maven - clean install,
 *    and asciidoctor-maven-plugin generate from asciidoc file an html for documentation
 */
// 

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {Main.class, MongoCustomerDaoImpl.class})//,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT )
public class CustomerTest {


    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;


    @Autowired
    private MongoCustomerDaoImpl customerRepository;

    @Autowired
    private Main main;

    private MockMvc mockMvc;

    private Customer myCust;
    private RestDocumentationResultHandler documentationHandler;

    //snippets properties
    private final static String SCHEMA = "https";
    private final static int PORT = 8445;

    @Before
    public  void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentation)
                        .uris().withPort(PORT).withScheme(SCHEMA))
                .build();

        this.customerRepository.deleteAllCustomers();
        myCust = new Customer("firstTest","test1@gmail.com","Rishon", "Sokolov", 22, new String[]{"32"});
        customerRepository.createCustomer(myCust);

    }

    //<editor-fold defaultstate="collapsed" desc=" Descriptors ">

    private static FieldDescriptor[] fullCustomerFields = new FieldDescriptor[]{
                        fieldWithPath("id").type(JsonFieldType.STRING).description("The customer's ID - cannot be null"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The customer's  name"),
                        fieldWithPath("city").type(JsonFieldType.STRING).description("The customer's city adress"),
                        fieldWithPath("street").type(JsonFieldType.STRING).description("The customer's street adress"),
                        fieldWithPath("houseNum").type(JsonFieldType.NUMBER).description("The customer's house number adress"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("The customer's email adress"),
                        fieldWithPath("tokens").type(JsonFieldType.ARRAY).description("The customer's cresit card tokens")};

    private static FieldDescriptor[] withoutKeyCustomerFields = new FieldDescriptor[]{
            fieldWithPath("id").description("The customer's ID - No need").optional(),
            fieldWithPath("name").type(JsonFieldType.STRING).description("The customer's  name"),
            fieldWithPath("city").type(JsonFieldType.STRING).description("The customer's city adress"),
            fieldWithPath("street").type(JsonFieldType.STRING).description("The customer's street adress"),
            fieldWithPath("houseNum").type(JsonFieldType.NUMBER).description("The customer's house number adress"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("The customer's email adress"),
            fieldWithPath("tokens").type(JsonFieldType.ARRAY).description("The customer's credit card tokens")};

    private static ParameterDescriptor[] customerPathParams = new  ParameterDescriptor[]{
            parameterWithName("custId").description("The customer's ID")
    };

    //</editor-fold>


    @Test
    public void getCustomer() throws Exception {
        this.mockMvc.perform(get("/customers/{custId}", myCust.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.myCust.getId())))
                .andExpect(jsonPath("$.name", is(this.myCust.getName())))
                .andExpect(jsonPath("$.city", is(this.myCust.getCity())))
                .andExpect(jsonPath("$.street", is(this.myCust.getStreet())))
                .andExpect(jsonPath("$.houseNum", is(this.myCust.getHouseNum())))
                .andExpect(jsonPath("$.tokens[0]", is(this.myCust.getTokens().toArray()[0])))
                .andDo(document("getCustomer", preprocessResponse(prettyPrint()), pathParameters(customerPathParams),responseFields(fullCustomerFields)));
    }

    @Test
    public void createCustomer() throws Exception {
        Customer createCust = new Customer("secondTest","test2@gmail.com","Rishon", "Tiumkin", 10, new String[]{"11"});
        String jsonCreateCustomer = asJsonString(createCust);
        ResultActions result = this.mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateCustomer))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andDo(document("createCustomer",preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), responseFields(fullCustomerFields),requestFields(withoutKeyCustomerFields)));;

    }

    @Test
    public void updateCustomer() throws Exception {
        this.myCust.setEmail("test3@gmail.com");
        String jsonMyCustomer = asJsonString(this.myCust);
        this.mockMvc.perform(put("/customers").contentType(MediaType.APPLICATION_JSON).content(jsonMyCustomer))
                .andExpect(status().isOk())
                .andDo(document("updateCustomer",preprocessRequest(prettyPrint()), requestFields(fullCustomerFields)));

    }

    @Test
    public void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/{custId}", myCust.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(""))
                .andDo(document("deleteCustomer",  pathParameters(customerPathParams)));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
