package property.app.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import property.app.beans.Adds;
import property.app.beans.InboxMessageGroup;
import property.app.beans.User;
import property.app.service.AddsService;
import property.app.service.UserService;
import property.app.beans.Message;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MessagesController {

    private UserService userService;
    private AddsService addsService;



    public MessagesController(UserService userService, AddsService addsService) {
        this.userService = userService;
        this.addsService = addsService;
    }

    @GetMapping("/messages")
    public String message(Model model) {
        List<User> u  = userService.getAll();
        model.addAttribute("friends", u);
        model.addAttribute("m", new Message());
        return "adds/messages";
    }

    @GetMapping("/message/inbox")
    public String messageInbox(Model model) {
        User currentUser  = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Message> messages  = userService.getMessagesByRecieverId(currentUser.getId());
        List<InboxMessageGroup> inboxMessageGroups = new ArrayList<>();
        HashMap<User, List<Message>> userListHashMap = new HashMap<>();
        for(Message mm : messages){
            if(mm!=null){
                if(userListHashMap.containsKey(mm.getSender())){
                    userListHashMap.get(mm.getSender()).add(mm);
                    userListHashMap.put(mm.getSender(), userListHashMap.get(mm.getSender()));
                }else {
                    ArrayList<Message> messages1 = new ArrayList<>();
                    messages1.add(mm);
                    userListHashMap.put(mm.getSender(), messages1);
                }
            }
        }

        Iterator it = userListHashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            InboxMessageGroup inboxMessageGroup = new InboxMessageGroup();
            inboxMessageGroup.setSender((User) pair.getKey());
            if(null!=pair.getValue()){
                Collections.reverse((List<Message>) pair.getValue());
            }
            inboxMessageGroup.setMessageList((List<Message>) pair.getValue());
            inboxMessageGroups.add(inboxMessageGroup);
            it.remove();
        }

        model.addAttribute("messages", inboxMessageGroups);
        model.addAttribute("m", new Message());
        return "adds/inbox";
    }

    @GetMapping("/message/outbox")
    public String messageOutbox(Model model) {
        User currentUser  = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<Message> messages  = userService.getMessagesBySenderId(currentUser.getId());
        if(null!=messages){
            Collections.reverse(messages);
        }
        model.addAttribute("messages", messages);
        return "adds/outbox";
    }
    @PostMapping("/message/submit")
    public String message(@RequestParam(value = "id") Long id, Message message, Model model) {
        Optional<User> u  = userService.getById(id);
        User user = u.get();
        message.setReceiver(user);
        User sender  = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        message.setSender(sender);
        userService.addMessage(message);
        List<User> uuu  = userService.getAll();
        model.addAttribute("friends", uuu);
        model.addAttribute("m", new Message());
        return "adds/messages";
    }

    @PostMapping("/message/add/submit")
    public String messageWithAdd(@RequestParam(value = "id") Long id, @RequestParam(value = "addId") Long addId, Message message, Model model) {
        Optional<User> u  = userService.getById(id);
        User user = u.get();
        message.setReceiver(user);
        User sender  = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        message.setSender(sender);
        if(message.getMessageText() == null || message.getMessageText().equals("")){
            message.setMessageText("");
        }
        Optional<Adds> post = addsService.findById(addId);
        if( post.isPresent() ) {
            Adds currentAdds = post.get();
            message.setMessageText(message.getMessageText()+" --- {This message belongs to Add title :"+currentAdds.getTitle()+" and id : "+addId+"}");
        }else{
            message.setMessageText(message.getMessageText()+" --- {This message belongs to Add id "+addId+"}");
        }
        userService.addMessage(message);

        List<Adds> adds = addsService.findAll();
        if(null!= adds){
            Collections.reverse(adds);
        }
        model.addAttribute("adds", adds);
        model.addAttribute("m", new Message());
        return "adds/list";
    }
}