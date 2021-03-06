package controller;

import model.ChrodUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import service.INexusService;
import utils.ResponseUtil;
import utils.ResponseVo;
import utils.ResultCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author zhangjian5
 * @Time 2016/9/6
 */
@Controller
public class NexusController {
    @Autowired
    private INexusService iNexusService;

    @RequestMapping("/header")
    public String header() {
        return "views/header";

    }

    @RequestMapping("/nexus")
    public ModelAndView nexus() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("mysqlStr", iNexusService.someTries().toString());
        modelAndView.setViewName("views/nexus");
        return modelAndView;
    }

//    @RequestMapping("/nexus/{imageId}")
//    @ResponseBody
//    public String nexusByte(
//            @PathVariable("imageId") String imageId) throws Throwable {
//        Resource resource = new ClassPathResource("QQ图片20160909170655.gif");
//        byte[] fileDate = FileCopyUtils.copyToByteArray(resource.getInputStream());
//        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(fileDate, org.springframework.http.HttpStatus.OK);
//        return Base64Utils.encodeToString(responseEntity.getBody());
//    }

    @RequestMapping("/nexus/view/tn_food")
    public ModelAndView tnFoodView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("views/tn_food");
        return modelAndView;
    }

    @RequestMapping("/nexus/tn_food/{area}")
    @ResponseBody
    public ResponseVo nexusTnFood(@PathVariable String area) {
        ResponseVo responseVo = ResponseUtil.buildVoByResultCode(false, ResultCode.FAILURE);
        responseVo = ResponseUtil.buildVoByResultCode(true, ResultCode.SUCCESS, iNexusService.customerData(area));
        return responseVo;
    }


    @RequestMapping(value = "/nexus/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(ChrodUser chrodUser, HttpServletResponse response, HttpServletRequest request, HttpSession session) {
        Subject user = SecurityUtils.getSubject();
        if ("POST".equals(request.getMethod())) {
            UsernamePasswordToken token = new UsernamePasswordToken(chrodUser.getUsername(), chrodUser.getPassword());
            try {
                user.login(token);
                return "redirect:view/tn_food";
            } catch (AuthenticationException ex) {
                return "views/login";
            }
        } else {
            if (user.isAuthenticated()) {
                return "redirect:view/tn_food";
            } else {
                return "views/login";
            }
        }

    }
}
