package property.app.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import property.app.beans.Comment;
import property.app.beans.Adds;
import property.app.beans.Message;
import property.app.beans.User;
import property.app.repository.CommentRepository;
import property.app.service.AddsService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class AddsController {

    private static final Logger logger = LoggerFactory.getLogger(AddsController.class);

    private AddsService addsService;
    private CommentRepository commentRepository;


    public AddsController(AddsService addsService, CommentRepository commentRepository) {
        this.addsService = addsService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/")
    public String list(Model model) {
        List<Adds> adds = addsService.findAll();
        if(null!= adds){
            Collections.reverse(adds);
        }
        model.addAttribute("adds", adds);
        model.addAttribute("m", new Message());
        return "adds/list";
    }

    @PostMapping("/search")
    public String messageWithAdd(Message message, Model model) {
        List<Adds> adds = addsService.search("%"+message.getMessageText()+"%");
        if(null!= adds){
            Collections.reverse(adds);
        }
        model.addAttribute("adds", adds);
        model.addAttribute("m", new Message());
        return "adds/list";
    }

    @GetMapping("/adds/{id}")
    public String read(@PathVariable Long id, Model model) {
        Optional<Adds> post = addsService.findById(id);
        if( post.isPresent() ) {
            Adds currentAdds = post.get();
            if(currentAdds.getComments()!=null){
                Collections.reverse(currentAdds.getComments());
            }
            Comment comment = new Comment();
            comment.setAdds(currentAdds);
            model.addAttribute("comment", comment);
            model.addAttribute("adds", currentAdds);
            model.addAttribute("success", model.containsAttribute("success"));
            model.addAttribute("m", new Message());
            return "adds/view";

        } else {
            return "redirect:/ ";
        }
    }

    @GetMapping("/adds/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        List<Comment> comments = commentRepository.findByAddsId(id);
        if(comments!=null){
            for(Comment comment : comments){
                commentRepository.deleteById(comment.getId());
            }
        }
        addsService.deleteById(id);
        return "redirect:/ ";
    }

    @GetMapping("/adds/submit")
    public String newPostForm(Model model){
        model.addAttribute("adds", new Adds());
        return "adds/submit";
    }

    @PostMapping("/adds/submit")
    public String createPost(@Valid Adds adds, @RequestParam("image") MultipartFile file, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Validation error while submitting a new add.");
            model.addAttribute("adds", adds);
            return "adds/submit";
        } else {
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    StringBuilder sb = new StringBuilder();
                    sb.append("data:image/png;base64,");
                    sb.append(StringUtils.newStringUtf8(
                            Base64.encodeBase64(bytes, false)));
                    String base64 = sb.toString();
                    adds.setImageBase64(base64);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            User u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            adds.setUser(u);
            adds.setIsPost("true");
            addsService.save(adds);
            logger.info("New Post was saved successfully.");
            redirectAttributes.addAttribute("id", adds.getId())
                    .addFlashAttribute("success", true);
            return "redirect:/adds/{id}";

        }
    }

    @Secured({"ROLE_USER"})
    @PostMapping("/adds/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            logger.info("There was a problem adding a new comment.");
        } else {
            commentRepository.save(comment);
            logger.info("New comment was saved.");
        }
        return "redirect:/adds/" + comment.getAdds().getId();
    }

}
