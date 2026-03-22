

@RestController
@RestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService service;
}
