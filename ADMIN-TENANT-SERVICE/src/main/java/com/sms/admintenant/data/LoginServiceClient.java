/*
 * package com.sms.admintenant.data;
 * 
 * import org.springframework.cloud.netflix.feign.FeignClient; import
 * org.springframework.web.bind.annotation.RequestHeader; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestMethod;
 * 
 * import com.sms.admintenant.models.User;
 * 
 * 
 * @FeignClient(value="LOGIN-SECURITY-SERVICE",url="localohost:8093/login")
 * public interface LoginServiceClient {
 * 
 * @RequestMapping(value = "/users/create", method = RequestMethod.POST,
 * consumes = "application/json") public boolean createUser(User
 * user, @RequestHeader String token);
 * 
 * }
 */