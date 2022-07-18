package com.example.carRetail.utility;
import com.example.carRetail.entity.Car;
import com.example.carRetail.exceptions.CSVException;
import com.example.carRetail.model.CarDto;
import com.example.carRetail.model.UserDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtility {

    private static final String[] FILE_HEADING_CAR = {"Inventory Id", "Registration Number", "Rto", "Registration " +
            "State",
            "Registration Year", "Mileage", "Make", "Model", "Body Type", "Car Score", "Variant", "Chassis " + "Number", "Colour", "Year Of Manufacture"};
    private static final String[] FILE_HEADING_USER = {"First Name", "Last Name", "Address", "Email Id", "Mobile Number", "Role"};

    private CSVUtility() {
    }

    public static boolean hasCSVFormat(MultipartFile file, String fileType) {
        return fileType.equals(file.getContentType());

    }

    public static List<CarDto> csvToCarDto(MultipartFile file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            return csvParser.getRecords().stream().map(i -> CarDto.builder().make(i.get(FILE_HEADING_CAR[6])).carScore(Integer.parseInt(i.get(FILE_HEADING_CAR[9]))).bodyType(i.get(FILE_HEADING_CAR[8])).chassisNumber(i.get(FILE_HEADING_CAR[11])).regNumber(i.get(FILE_HEADING_CAR[1])).colour(i.get(FILE_HEADING_CAR[12])).model(i.get(FILE_HEADING_CAR[7])).registrationState(i.get(FILE_HEADING_CAR[3])).registrationYear(Integer.parseInt(i.get(FILE_HEADING_CAR[4]))).mileage(Integer.parseInt(i.get(FILE_HEADING_CAR[5]))).yearOfManufacture(Integer.parseInt(i.get(FILE_HEADING_CAR[13]))).rto(i.get(FILE_HEADING_CAR[2])).variant(i.get(FILE_HEADING_CAR[10])).build()).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new CSVException("Invalid CSV file: please match all the headings in the file with," + Arrays.stream(FILE_HEADING_CAR).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CSVException("failed to parse CSV file");
        }
    }

    public static ByteArrayInputStream byteArrayInputStreamToCSV(List<Car> carList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord((Object[]) FILE_HEADING_CAR);
            carList.stream().map(car -> Arrays.asList(car.getId(), car.getRegNumber(), car.getRto(),
                    car.getRegistrationState(),
                    String.valueOf(car.getRegistrationYear()), String.valueOf(car.getMileage()), car.getBodyType(), String.valueOf(car.getCarScore()), car.getMake(), car.getModel(), car.getVariant(), car.getChassisNumber(), car.getColour(), String.valueOf(car.getYearOfManufacture()))).forEach(data -> {
                try {
                    csvPrinter.printRecord(data);
                } catch (IOException e) {
                    throw new CSVException("Unable to write data to a CSV file");
                }
            });

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new CSVException("Unable to write data to a CSV file");
        }
    }

    public static List<UserDto> csvToUserDto(MultipartFile file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)); CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            return csvParser.getRecords().stream().map(i -> UserDto.builder().firstName(i.get(FILE_HEADING_USER[0])).lastName(i.get(
                    FILE_HEADING_USER[1])).address(i.get(FILE_HEADING_USER[2])).email(i.get(FILE_HEADING_USER[3])).mobileNumber(i.get(FILE_HEADING_USER[4])).role(i.get(
                    FILE_HEADING_USER[5])).build()).collect(Collectors.toList());

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new CSVException("Invalid CSV file: please match all the headings in the file with," + Arrays.stream(FILE_HEADING_USER).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CSVException("failed to parse CSV file");
        }
    }
}

