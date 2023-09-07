package com.example.demo.Services;

import com.example.demo.Repository.*;
import com.example.demo.modle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class AnonymousService extends ServiceBase implements IAnonymousService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private AirlineCompaniesRepository airlineCompaniesRepository;
    @Autowired
    private AdministratorsRepository administratorsRepository;
    @Autowired
    private FlightsRepository flightsRepository;

    @Value("${min_PasswordLength}")
    private Integer min_PasswordLength;

    @Value("${spring.datasource.mailPassword}")
    private String mailPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Users login(String username, String password) throws UserNotFoundException, InvalidPasswordException {
        Users user = usersRepository.getByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + username);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }

        return user;
    }


    public boolean isUsernameTaken(String username) {
        return usersRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return usersRepository.existsByEmail(email);
    }


    public boolean isPhoneTaken(String phone) {
        return customersRepository.existsByPhone(phone);
    }

    public boolean isCreditCardTaken(String creditCard) {
        return customersRepository.existsByCreditCard(creditCard);
    }

    public boolean isNameTaken(String name) {
        return airlineCompaniesRepository.existsByName(name);
    }

public boolean forgotPassword(String email){
        Users user = usersRepository.getUserByEmail(email);
        if (user != null){
            String newPassword =generateRandomPassword();
            usersRepository.updatePassword(passwordEncoder.encode(newPassword),user.getId());
            sendNewPasswordByEmail(email, newPassword);
            return true;

        }
        return false;
}

    private void sendNewPasswordByEmail(String email, String newPassword) {
        // Configure your email server settings
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Set your email username and password
        String username = "tal1546789@gmail.com";
        String password = mailPassword;

        // Create a session with the email server
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("SAFE FLIGHT - Password Reset");
            message.setText("Your new password is: " + newPassword  + "\n\n Thank you for choosing SAFE FLIGHT");

            // Send the email
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



private String generateRandomPassword(){
    Random random = new Random();
    String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    // Generate a random password length between 7 and 12 characters (inclusive)
    int passwordLength = random.nextInt(min_PasswordLength) + 7;

    String password = "";
    for (int i = 0; i < passwordLength; i++) {
        int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
        char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
        password+= randomChar;
    }

    return password;
}



    public UserWithCustomerDTO createNewCustomer(UserWithCustomerDTO userWithCustomerDTO) throws UsernameAlreadyExistsException, EmailAlreadyRegisteredException, WeakPasswordException {
        validetorUser(userWithCustomerDTO.getUser());
        userWithCustomerDTO.getUser().setPassword(passwordEncoder.encode(userWithCustomerDTO.getUser().getPassword()));
        Users user = usersRepository.add(userWithCustomerDTO.getUser());
        Customers customer = userWithCustomerDTO.getCustomer();
        customer.setUser_id(user.getId());
        customersRepository.add(customer);
        return userWithCustomerDTO;

    }

    public UserWithAirlineCompanyDTO createNewAirlineCompany(UserWithAirlineCompanyDTO airlineCompanyDTO) throws UsernameAlreadyExistsException, EmailAlreadyRegisteredException, WeakPasswordException {
        validetorUser(airlineCompanyDTO.getUser());
        airlineCompanyDTO.getUser().setPassword(passwordEncoder.encode(airlineCompanyDTO.getUser().getPassword()));
        Users user = usersRepository.add(airlineCompanyDTO.getUser());
        AirlineCompanyDetails airlineCompany = airlineCompanyDTO.getAirlineCompany();
        airlineCompany.setUser_id(user.getId());
        AirlineCompanies createdAirlineComapny = new AirlineCompanies(airlineCompany.getId(),airlineCompany.getName(),
                airlineCompaniesRepository.getCountryIdByName(airlineCompany.getCountry()),airlineCompany.getUser_id());
        airlineCompaniesRepository.add(createdAirlineComapny);
        return airlineCompanyDTO;

    }

    public UserWithAdministratorDTO createNewAdministrator(UserWithAdministratorDTO administratorDTO) throws UsernameAlreadyExistsException, EmailAlreadyRegisteredException, WeakPasswordException {
        validetorUser(administratorDTO.getUser());
        administratorDTO.getUser().setPassword(passwordEncoder.encode(administratorDTO.getUser().getPassword()));
        Users user = usersRepository.add(administratorDTO.getUser());
        Administrators administrator = administratorDTO.getAdministrator();
        administrator.setUser_id(user.getId());
        administratorsRepository.add(administrator);
        return administratorDTO;
    }


    public List<FlightDetails> getSearchFlights(Date date, String originCountry, String destinationCountry, Integer[] airlineIdArray) {
        Integer originCountryId = null;
        Integer destinationCountryId = null;

        if (!originCountry.equals("")) {
             originCountryId = airlineCompaniesRepository.getCountryIdByName(originCountry);
        }
         if (!destinationCountry.equals("")) {

             destinationCountryId = airlineCompaniesRepository.getCountryIdByName(destinationCountry);
        }

        List<Integer> airlineIdList = new ArrayList<>();
        if (airlineIdArray != null) {
            airlineIdList = Arrays.stream(airlineIdArray).toList();
        }


        List<Flights> flightsList = flightsRepository.getSearchFlights(date, originCountryId, destinationCountryId, airlineIdList);
        List<FlightDetails> flightDetails = new ArrayList<>();
        for (Flights flight : flightsList){
            flightDetails.add(new FlightDetails(flight.getId(), airlineCompaniesRepository.getById(flight.getAirlineCompanyId()).getName(),
                    airlineCompaniesRepository.getCountryNameByCountryId(flight.getOriginCountryId()),airlineCompaniesRepository.getCountryNameByCountryId(flight.getDestinationCountryId()),
                    flight.getDepartureTime(),flight.getLandingTime(),flight.getRemainingTickets()));
        }
        return flightDetails;
    }

    public List<AirlineCompanyDetails> getAllAirlineWithDetails(){
        List <AirlineCompanies> airlineCompaniesList = airlineCompaniesRepository.getAll();
        List <AirlineCompanyDetails> airlineCompanyDetailsList = new ArrayList<>();
        for (AirlineCompanies airlineCompany : airlineCompaniesList){
            airlineCompanyDetailsList.add(new AirlineCompanyDetails(airlineCompany.getId(),airlineCompany.getName()
                    ,airlineCompaniesRepository.getCountryNameByCountryId(airlineCompany.getCountryId()),airlineCompany.getUserId()));
        }
        return airlineCompanyDetailsList;
    }

    private void validetorUser(Users user) throws UsernameAlreadyExistsException, EmailAlreadyRegisteredException, WeakPasswordException{
        // Check if the user already exists
        Users existingUser = usersRepository.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        // Check if the email is already registered
        Users existingEmailUser = usersRepository.getUserByEmail(user.getEmail());
        if (existingEmailUser != null) {
            throw new EmailAlreadyRegisteredException("Email already registered");
        }

        // Check if the password meets the minimum length requirement
        if (user.getPassword().length() < min_PasswordLength) {
            throw new WeakPasswordException("Password must be at least 6 characters long");
        }
    }
}

