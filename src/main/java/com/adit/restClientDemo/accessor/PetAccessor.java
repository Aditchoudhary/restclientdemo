package com.adit.restClientDemo.accessor;

import lombok.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;

public class PetAccessor {

    static String URL = "https://jsonplaceholder.typicode.com/todos/{id}";

    static String POST_URL = "https://jsonplaceholder.typicode.com/posts";
    static RestTemplate restTemplate = new RestTemplate();

    static RestClient restClient = RestClient.create();

    public  static  void getPet(){

        testStringFormatResponse();

        testResponseEntityFormatResponse();

        Todo todo = new Todo(1, 1 , "foo", true);
        ResponseEntity<Todo> voidResponseEntity =  restClient
                .post()
                .uri(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(todo)
                .retrieve()
                .toEntity(Todo.class);

        System.out.println("post request status : " + voidResponseEntity.getStatusCode());
        System.out.println("post request body : " + voidResponseEntity.getBody());



        String responseHandlingError = restClient.get()
                .uri(URL, 1)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {

                        throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders());
                }).onStatus(HttpStatusCode::is5xxServerError, ((request, response) -> {
                        throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders());
                }))
                .body(String.class);










    }

    private static void testResponseEntityFormatResponse() {

        ResponseEntity<Todo> clientResponseEntity = restClient.get()
                .uri(URL, 1)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(Todo.class);

        System.out.println("Entity Response Status : " + clientResponseEntity.getStatusCode());
        System.out.println("Entity Response Header : " + clientResponseEntity.getHeaders());
        System.out.println("Entity Response Entity : " + clientResponseEntity.getBody());

        System.out.println();
        System.out.println("********************* Rest Template using getForEntity ***************");
        System.out.println();


        ResponseEntity<Todo> templateResponseEntity = restTemplate.getForEntity(URL, Todo.class, 1);
        System.out.println("Entity Response Status : " + templateResponseEntity.getStatusCode());
        System.out.println("Entity Response Header : " + templateResponseEntity.getHeaders());
        System.out.println("Entity Response Entity : " + templateResponseEntity.getBody());
    }

    private static void testStringFormatResponse() {


        String  clientStringResponse = restClient.get()
                .uri(URL, 1)
                .retrieve()
                .body(String.class);
        System.out.println("String Response : " + clientStringResponse);


        System.out.println();
        System.out.println("********************* Rest Template using Get Object ***************");
        System.out.println();

        Todo templateGetObject = restTemplate.getForObject(URL, Todo.class, 1);
        System.out.println("templateGetObject : " + templateGetObject);


        System.out.println();
        System.out.println("********************* Rest Template using getForEntity ***************");
        System.out.println();


        ResponseEntity<String> templateResponseEntityAsString = restTemplate.getForEntity(URL, String.class, 1);
        System.out.println("Entity Response Status : " + templateResponseEntityAsString.getStatusCode());
        System.out.println("Entity Response Header : " + templateResponseEntityAsString.getHeaders());
        System.out.println("Entity Response Entity : " + templateResponseEntityAsString.getBody());

    }

    public static void main(String[] args) {
        getPet();
    }



}

@ToString
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
class Todo {
    int userId;
    int id;
    String title;
    boolean completed;
}

/**
 *
 * {
 *   "userId": 1,
 *   "id": 1,
 *   "title": "delectus aut autem",
 *   "completed": false
 * }
 *
 */