package com.farmconnect.krishisetu.users_management.controllers;

//import org.springframework.security.core.userdetails.User as SpringSecurityUser;
import com.farmconnect.krishisetu.security.service.UserDetailsServiceImpl;
import com.farmconnect.krishisetu.users_management.model.SkillProfile;
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;
import com.farmconnect.krishisetu.users_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private UserService userService;


    /*     * User Management APIs
     */

    @GetMapping("hello")
    public String helloUser(){
        return userService.helloUser();
    };


    // @GetMapping("/profile/")
    // public ResponseEntity<?> getUserProfile(){
    //     return userService.getUserProfile();
    // }

    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        
        // You now have direct access to all fields defined in your CustomUserDetails
        // String userId = userDetails.getId();
        // String username = userDetails.getUsername();
        

        // Pass the identifier to the service layer to fetch the complete profile
        return userService.getUserProfile(springUser);
    }
    
    // @PostMapping("/register")
    // public ResponseEntity<?> registerUser(@RequestBody UserProfile request){
    //     return userService.registerUser(request);
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> loginUser(@RequestBody ){
    //     return userService.loginUser(request); 
    // }

    // @PostMapping("/update/profile/{email}/{role}")
    // public ResponseEntity<?> updateUserProfile(@PathVariable String email, @PathVariable String role, @RequestBody UserProfileSuperSet request){
    //     return userService.updateUserProfile(email, role, request);
    // }

    
    // /*     * Worker Management APIs
    //  */
    @PreAuthorize("hasRole('FARMER')")
    @PostMapping("filter/workers/available/{email}")
    public ResponseEntity<List<WorkerProfile>> getActiveWorkers(@PathVariable String email){
        return userService.getActiveWorkers(email);
    }

    // @PostMapping("/workers/by/location/{radius}")
    // public ResponseEntity<List<WorkerProfile>> getWorkersInRadius(@RequestBody WorkerFilterRequest request){
    //     return userService.getWorkersInRadius(request);
    // }

   
    @PostMapping("filter/workers/jobtype/{email}/{jobType}")
    public ResponseEntity<List<WorkerProfile>> getWorkersByJobType(@PathVariable String email,@PathVariable String jobType){
        return userService.getWorkersByJobType(jobType);
    }


    @PostMapping("filter/workers/byskills/{email}")
    public ResponseEntity<List<WorkerProfile>> getWorkersBySkills(@PathVariable String email,@RequestBody List<String> skillReq){
        return userService.getWorkersBySkills(email, skillReq);
    }

    @GetMapping("get/worker/skills/{email}")
    public ResponseEntity<List<SkillProfile>> getWorkersSkills(@PathVariable String email){
        return userService.getWorkersSkills(email);
    }

    @PostMapping("update/worker/status/{email}/{status}")
    public ResponseEntity<WorkerProfile> updateWorkerStatus(@PathVariable String email, @PathVariable String status){
        return userService.updateWorkerStatus(email,status);
    }

    


}
