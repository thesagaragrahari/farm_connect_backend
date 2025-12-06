package com.farmconnect.krishisetu.users_management.controllers;


import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.reqres.UserRegistrationRequest;
import com.farmconnect.krishisetu.users_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    /*     * User Management APIs
     */

    @GetMapping("/hello")
    public String helloUser(){
        return userService.helloUser();
    };

    @GetMapping("/profile/{userId}/{role}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId, @PathVariable String role){
        return userService.getUserProfile(userId, role);
    }
    
    // @PostMapping("/register/{role}")
    // public ResponseEntity<?> registerUser(@PathVariable String role, @RequestBody UserRegistrationRequest request){
    //     return userService.registerUser(role, request);
    // }

    // @PostMapping("/login")
    // public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest request){
    //     return userService.loginUser(request); 
    // }

    // @PostMapping("/update/profile/{userId}/{userType}")
    // public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long userId, @PathVariable String userType, @RequestBody UserProfileUpdateRequest request){
    //     return userService.updateUserProfile(userId, userType, request);
    // }

    
    // /*     * Worker Management APIs
    //  */
    // @PostMapping("/workers/active")
    // public ResponseEntity<List<WorkerProfile>> getActiveWorkers(@RequestBody WorkerFilterRequest request){
    //     return userService.getActiveWorkers(request);
    // }

    // @PostMapping("/workers/by/location/{radius}")
    // public ResponseEntity<List<WorkerProfile>> getWorkersInRadius(@RequestBody WorkerFilterRequest request){
    //     return userService.getWorkersInRadius(request);
    // }

    // @PostMapping("/workers/update/status/{workerId}")
    // public ResponseEntity<WorkerProfile> updateWorkerStatus(@PathVariable Long workerId){
    //     return userService.updateWorkerStatus(workerId);
    // }

    // @PostMapping("/workers/jobtype/{jobType}")
    // public ResponseEntity<List<WorkerProfile>> getWorkersByJobType(@PathVariable String jobType, @RequestBody WorkerFilterRequest request){
    //     return userService.getWorkersByJobType(jobType, request);
    // }

    // @PostMapping("/workers/skills")
    // public ResponseEntity<List<WorkerProfile>> getWorkersBySkills(@RequestBody WorkerSkillsFilterRequest request){
    // return userService.getWorkersBySkills(request);
    // }
    


}
