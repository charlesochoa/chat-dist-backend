package chatdist.backend;

import chatdist.backend.api.AuxiliarController;
import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.util.Scanner;

public class AuxiliarTestApplication {



    public static void main(String[] args) throws Exception {
        SpringApplication.run(BackendApplication.class, args);
        AuxiliarController t = new AuxiliarController();

    }
}
