package com.example.productservice.command.rest;

import com.example.productservice.command.CreateProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    private final CommandGateway commandGateway;

    @Autowired
    public ProductCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

//ข้อ1-3
//    @PostMapping
//    public String createProduct(@RequestBody CreateProductRestModel model){
//        return "Product created" + model.getTitle();
//    }

    //ข้อ4
    @PostMapping
    public String createProduct(@RequestBody CreateProductRestModel model){

        CreateProductCommand command = CreateProductCommand.builder().productId(UUID.randomUUID().toString())
                .title(model.getTitle()).price(model.getPrice()).quantity(model.getQuantity()).build();
        String result;
        try {
            result = commandGateway.sendAndWait(command);
        }catch (Exception e){
            result = e.getLocalizedMessage();
        }
        return  result;
    }

    @DeleteMapping
    public String deleteProduct(){
        return "Product Deleted";
    }
}
