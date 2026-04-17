package com.farmconnect.krishisetu.modules.user_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.farmconnect.krishisetu.modules.user_service.model.FarmerProfile;
import com.farmconnect.krishisetu.modules.user_service.model.SkillProfile;
import com.farmconnect.krishisetu.modules.user_service.model.UserProfile;
import com.farmconnect.krishisetu.modules.user_service.model.WorkerProfile;
import com.farmconnect.krishisetu.modules.user_service.services.UserService;

import static com.farmconnect.krishisetu.common.util.Routes.FARMER;
import static com.farmconnect.krishisetu.common.util.Routes.USER;

import java.util.List;




@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/complete-profile/worker")
    public ResponseEntity<String> registerWorker(
            @RequestBody WorkerProfile profile) {

        return userService.completeWorkerProfile(profile);
    }

    @PostMapping("/complete-profile/farmer")
    public ResponseEntity<String> registerFarmer(
            @RequestBody FarmerProfile profile) {

        return userService.completeFarmerProfile(profile);
    }

    /*     * User Management APIs
     */

    @GetMapping("hello")
    public String helloUser(){
        return userService.helloUser();
    };

    @PreAuthorize("isAuthenticated()")  
    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {
        return userService.getUserProfile(springUser);
    }

    @PreAuthorize("isAuthenticated()")  
    @PutMapping("/profile")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser,@RequestBody UserProfile uProfile) {
        return userService.updateUserProfile(springUser,uProfile);
    }

    @PreAuthorize("isAuthenticated()")  // change pass for authenticated user
    @PutMapping(USER+"/password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser,@RequestBody String newPassword) {
        return userService.updatePassword(springUser,newPassword);
    }





    /* farmer management apis */
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping(FARMER+"/profile/")
    public ResponseEntity<?> updateFarmerProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser,
                                                 @RequestBody FarmerProfile updatedProfile) {
        return userService.updateFarmerProfile(springUser, updatedProfile);
    }
    
    

    
    // /*     * Worker Management APIs
    //  */
    @PreAuthorize("hasRole('FARMER')")
    @PostMapping("filter/workers/available")
    public ResponseEntity<List<WorkerProfile>> getActiveWorkers(){
        return userService.getActiveWorkers();
    }

    // @PostMapping("/workers/by/location/{radius}")
    // public ResponseEntity<List<WorkerProfile>> getWorkersInRadius(@RequestBody WorkerFilterRequest request){
    //     return userService.getWorkersInRadius(request);
    // }

   
    @PostMapping("filter/workers/jobtype/{jobType}")
    public ResponseEntity<List<WorkerProfile>> getWorkersByJobType(@PathVariable String jobType){
        return userService.getWorkersByJobType(jobType);
    }


    @PostMapping("filter/workers/byskills")
    public ResponseEntity<List<WorkerProfile>> getWorkersBySkills(@RequestBody List<String> skillReq){
        return userService.getWorkersBySkills(skillReq);
    }

    @GetMapping("get/worker/skills")
    public ResponseEntity<List<SkillProfile>> getWorkersSkills(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser){
        return userService.getWorkersSkills(springUser.getUsername());
    }

    @PostMapping("update/worker/status/{status}")
    public ResponseEntity<WorkerProfile> updateWorkerStatus(@AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser, @PathVariable String status){
        return userService.updateWorkerStatus(springUser.getUsername(),status);
    }

    


}
