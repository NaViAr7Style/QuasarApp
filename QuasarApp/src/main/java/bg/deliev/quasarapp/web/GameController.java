package bg.deliev.quasarapp.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
public class GameController {

//    @GetMapping("/{id}")
//    public String details(@PathVariable("id") Long id,
//                          Model model,
//                          @AuthenticationPrincipal UserDetails viewer) {
//
//        OfferDetailDTO offerDetailDTO = offerService
//                .getOfferDetails(uuid, viewer)
//                .orElseThrow(() -> new ObjectNotFoundException("Offer with UUID " + uuid + " was not found!"));
//
//        model.addAttribute("offer", offerDetailDTO);
//
//        return "details";
//    }
}
