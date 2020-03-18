package filesOperations;

import javafx.stage.FileChooser;
import userPackage.Person;
import userPackage.UserCardNumber;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileWriter {
    private List<Person> listOfPeople = null;
    private List<UserCardNumber> listOfCarNumbers = null;

    public FileWriter(List<Person> peopleData,List<UserCardNumber> cardNumbers) {
        listOfPeople = peopleData;
        listOfCarNumbers = cardNumbers;
    }

    private Path chooseFile(){
        FileChooser fileChooser = new FileChooser();
        Path workingDirectory = Paths.get(".");
        fileChooser.setInitialDirectory(workingDirectory.toFile());
        File file = fileChooser.showOpenDialog(null);

        if(file == null)
            return null;
        return file.toPath();
    }

    public void writeUserDataToFile() throws SQLException {
        Path workingFile = chooseFile();
        if( workingFile == null)
            return;

        List<String> dataToBeWrittenInFile = listOfPeople
                .stream()
                .map(Person::toString)
                .collect(Collectors.toList());

        try {
            Files.write(workingFile, dataToBeWrittenInFile, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Error with writing in file");
        }
      }

      public void writeCardDataSortedByFilter(Comparator comparator){
        Path workingFile = chooseFile();
        if(workingFile == null)
            return;

        List<String> dataToBeWrittenInFile = (List<String>) listOfCarNumbers
                .stream()
                .sorted(comparator)
                .map( x -> String.format("%s",x.toString()))
                .collect(Collectors.toList());

        try{
            Files.write(workingFile,dataToBeWrittenInFile,Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Error with writing in file");
        }
      }
    }