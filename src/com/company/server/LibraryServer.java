package com.company.server;

import com.company.Employee;
import com.company.models.BookDataModel;
import com.company.models.BookModel;
import com.company.models.EmployeesDataModel;
import com.company.models.EmployeeModel;
import com.company.utils.Utils;
import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import com.company.Book;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.company.context.DataContext;

public class LibraryServer extends BasicServer{
    private final static Configuration freemarker = initFreeMarker();
    private final DataContext dataContext;
    private EmployeesDataModel employeeDataModel = new EmployeesDataModel();
    private BookDataModel bookDataModel = new BookDataModel();

    public LibraryServer(String host, int port, DataContext dataContext) throws IOException {
        super(host, port);
        this.dataContext = dataContext;
        registerGet("/", this::indexHandler);
        registerGet("/main", this::mainHandler);
        registerGet("/java", this::bookHandler);
        registerGet("/javascript", this::bookHandler);
        registerGet("/python", this::bookHandler);
        registerGet("/csharp", this::bookHandler);
        registerGet("/employee", this::employeeHandler);
        registerGet("/employees", this::employeesHandler);

        registerGet("/register", this::registrationGet);
        registerPost("/register", this::registrationPost);

        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);

        registerGet("/success", exchange -> sendFile(exchange, makeFilePath("success_register.html"), ContentType.TEXT_HTML));
        registerGet("/wrong", exchange -> sendFile(exchange, makeFilePath("incorrectUser_register.html"), ContentType.TEXT_HTML));

        registerGet("/wrong_user", exchange -> sendFile(exchange, makeFilePath("incorrectUser_login.html"), ContentType.TEXT_HTML));
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

            cfg.setDirectoryForTemplateLoading(new File("data"));

            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void employeeHandler(HttpExchange exchange) {
        Employee newEmployee = null;
        String query = exchange.getRequestURI().getQuery();
        if (query == null){
            newEmployee = new Employee("Некий пользователь", "test@test.ru");

        } else {
            String email = query.split("=")[1];
            for (int i = 0; i < employeeDataModel.getEmployees().size(); i++) {
                if (employeeDataModel.getEmployees().get(i).getEmail().equals(email)) {
                    newEmployee = employeeDataModel.getEmployees().get(i);
                }
            }
        }
        renderTemplate(exchange, "templates/employeeTemplate.html", getEmployeeModel(newEmployee));
    }

    private Object getEmployeeModel(Employee employee) {
        return new EmployeeModel(employee);
    }

    private void employeesHandler(HttpExchange exchange){
        renderTemplate(exchange, "templates/employees_list.html", getEmployeeDataModel());
    }
    private void bookHandler(HttpExchange exchange) {
        Book newBook = null;
        String path = exchange.getRequestURI().getPath();
        String uri = path.substring(1, path.length());
        for (int i = 0; i < bookDataModel.getBooks().size(); i++) {
            if (bookDataModel.getBooks().get(i).getName().equals(uri)){
                newBook = bookDataModel.getBooks().get(i);
            }
        }
        renderTemplate(exchange, "templates/bookTemplate.html", getBookModel(newBook));
    }

    private void indexHandler(HttpExchange exchange) {
        renderTemplate(exchange, "index.html", getBookDataModel());
    }

    private void mainHandler(HttpExchange exchange) {
        for (int i = 0; i < employeeDataModel.getEmployees().size(); i++) {
            for (int j = 0; j < bookDataModel.getBooks().size(); j++) {
                if (employeeDataModel.getEmployees().get(i).getBooksOnHand().contains(bookDataModel.getBooks().get(j))){
                    bookDataModel.getBooks().get(j).setAvailable(false);
                }
            }
        }
        renderTemplate(exchange, "main_page.html", getBookDataModel());
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private EmployeesDataModel getEmployeeDataModel() {
        return new EmployeesDataModel();
    }

    private BookDataModel getBookDataModel() {
        return new BookDataModel();
    }

    private BookModel getBookModel(Book book) {
        return new BookModel(book);
    }

//    REGISTRATION
    private void registrationGet(HttpExchange exchange) {
        Path path = makeFilePath("registration_form.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    protected void registerPost(String route, RouteHandler handler){
        getRoutes().put("POST " + route, handler);
    }

    private void registrationPost(HttpExchange exchange) {
        String cType = getContentType(exchange);

        String raw = getBody(exchange);

        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        String name = parsed.get("username");
        String email = parsed.get("email");
        String password = parsed.get("password");

        Employee newEmployee = new Employee(name, email);
        newEmployee.setPassword(password);
        for (int i = 0; i < employeeDataModel.getEmployees().size(); i++) {
            if (employeeDataModel.getEmployees().get(i).getEmail().equals(newEmployee.getEmail())){
                redirect303(exchange, "/wrong");
                return;
            }
        }

        employeeDataModel.getEmployees().add(newEmployee);
        dataContext.addUser(newEmployee);
        System.out.println(employeeDataModel.getEmployees());

        redirect303(exchange, "/success");
    }

    private String getBody(HttpExchange exchange) {
        InputStream input = exchange.getRequestBody();
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);

        try(BufferedReader bufferedReader = new BufferedReader(reader)){
            return bufferedReader.lines().collect(Collectors.joining(""));
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    private String getContentType(HttpExchange exchange) {
        return exchange.getRequestHeaders().getOrDefault("Content-type", List.of("")).get(0);
    }

    public void redirect303(HttpExchange exchange, String path){
        try {

            exchange.getResponseHeaders().add("Location", path);
            exchange.sendResponseHeaders(303, 0);
            exchange.getResponseBody().close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

//LOGIN
    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("login_form.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    private void loginPost(HttpExchange exchange) {
        String cType = getContentType(exchange);

        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");

        String name = parsed.get("username");
        String email = parsed.get("email");
        String password = parsed.get("password");


        for (int i = 0; i < employeeDataModel.getEmployees().size(); i++) {
            if (employeeDataModel.getEmployees().get(i).getEmail().equals(email)){
                redirect303(exchange, "/employee?email=" + email);
                return;
            }

        }
        Employee unknownEmployee = new Employee("Некий пользователь", "test@mail.com");
        employeeDataModel.setEmployee(unknownEmployee);
        redirect303(exchange, "/employee");
    }
}
