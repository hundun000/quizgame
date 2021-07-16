package hundun.quizgame.core.prototype;

import java.util.List;
import java.util.Set;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2021/06/28
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamPrototype {
    private String name;
    
    private List<String> pickTags;
    
    private List<String> banTags;
    
    private RolePrototype rolePrototype;
    
}
