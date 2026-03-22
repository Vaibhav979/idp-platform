

@RestController
@RestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService service;

    @PostMapping
    public Project create(@RequestBody Project project) {
        return service.createProject
    }
}
