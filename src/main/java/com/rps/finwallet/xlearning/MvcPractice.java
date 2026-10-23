package com.rps.finwallet.xlearning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * 🌐 SPRING MVC (MODEL - VIEW - CONTROLLER) ARCHITECTURE GUIDE & SIMULATION
 * ============================================================================
 * Spring MVC ka pura flow:
 * 1. Client HTTP Request bhejta hai.
 * 2. DispatcherServlet (Front Controller) request receive karta hai.
 * 3. HandlerMapping sahi Controller aur Method dhoondhta hai.
 * 4. Controller Service/DB se data lake "Model" me daalta hai.
 * 5. ViewResolver HTML Template (Thymeleaf) render karta hai, YA
 *    @RestController direct JSON return karta hai!
 * ============================================================================
 */
public class MvcPractice implements PracticeRunner {

    @Override
    public String getTitle() {
        return "Spring MVC Architecture (DispatcherServlet, Model, View, Controller, REST)";
    }

    @Override
    public void run() {
        explainMvcArchitectureFlow();
        explainClassicMvcVsRestMvc();
        simulateClassicMvcFlow();
        simulateRestMvcFlow();
        explainGlobalExceptionHandler();
    }

    // =========================================================================
    // 1. Architecture Flow
    // =========================================================================
    private void explainMvcArchitectureFlow() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          SPRING MVC (MODEL-VIEW-CONTROLLER) ARCHITECTURE FLOW                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("             HTTP Request (e.g. GET /wallets/101)");
        System.out.println("                         │");
        System.out.println("                         ▼");
        System.out.println("             ┌───────────────────────┐");
        System.out.println("             │   DispatcherServlet   │  <── [FRONT CONTROLLER: Gateway to all requests]");
        System.out.println("             └───────────┬───────────┘");
        System.out.println("                         │");
        System.out.println("          1. Which Controller to call?");
        System.out.println("                         ▼");
        System.out.println("             ┌───────────────────────┐");
        System.out.println("             │    HandlerMapping     │  <── Matches URL pattern with @GetMapping/@PostMapping");
        System.out.println("             └───────────┬───────────┘");
        System.out.println("                         │");
        System.out.println("          2. Invokes Controller Method");
        System.out.println("                         ▼");
        System.out.println("             ┌───────────────────────┐");
        System.out.println("             │      Controller       │  <── Handles request, calls Service & Repository");
        System.out.println("             └───────────┬───────────┘");
        System.out.println("                         │");
        System.out.println("             3. Returns Model + View Name (or direct JSON)");
        System.out.println("                         │");
        System.out.println("         ┌───────────────┴───────────────┐");
        System.out.println("         ▼                               ▼");
        System.out.println("  [CLASSIC MVC: HTML]             [REST API: JSON]");
        System.out.println("  @Controller + Thymeleaf         @RestController + Jackson");
        System.out.println("  ViewResolver renders HTML       Direct JSON serialized to Client");
    }

    // =========================================================================
    // 2. Difference: @Controller vs @RestController
    // =========================================================================
    private void explainClassicMvcVsRestMvc() {
        System.out.println("\n📌 [PART 1] @Controller vs @RestController (Difference Cheat Sheet)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("┌──────────────────┬─────────────────────────────┬─────────────────────────────┐");
        System.out.println("│ Feature          │ Classic @Controller         │ Modern @RestController      │");
        System.out.println("├──────────────────┼─────────────────────────────┼─────────────────────────────┤");
        System.out.println("│ Output Type      │ HTML Webpages (Thymeleaf/JSP│ Pure JSON / XML Data        │");
        System.out.println("│ Typical Consumer │ Web Browser (Server-rendered│ React, Angular, Mobile Apps │");
        System.out.println("│ Return Value     │ String (View file name)     │ Java Object / ResponseEntity│");
        System.out.println("│ Model Binding    │ Uses 'org.springframework.  │ Returns DTO/POJO directly   │");
        System.out.println("│                  │ ui.Model' (addAttribute)    │ (Jackson converts to JSON)  │");
        System.out.println("│ Formula          │ @Controller                 │ @Controller + @ResponseBody │");
        System.out.println("└──────────────────┴─────────────────────────────┴─────────────────────────────┘");
    }

    // =========================================================================
    // 3. Simulation 1: Classic MVC (@Controller + Model + Thymeleaf View)
    // =========================================================================
    private void simulateClassicMvcFlow() {
        System.out.println("\n📌 [PART 2] SIMULATION: CLASSIC MVC FLOW (@Controller + Model)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Scenario: User opens 'http://localhost:8080/wallet/view/101' in Browser");
        System.out.println();
        System.out.println("👉 Step 1: Controller Method Runs:");
        System.out.println("""
               @Controller
               public class WalletViewController {
                   @GetMapping("/wallet/view/{id}")
                   public String showWallet(@PathVariable Long id, Model model) {
                       Wallet wallet = walletService.getWallet(id);
                       model.addAttribute("walletNumber", "WLT-9988");
                       model.addAttribute("balance", 25400.00);
                       return "wallet-details"; // <-- HTML file name in templates/
                   }
               }
            """);

        // Simulating Spring Model map
        Map<String, Object> springModel = new HashMap<>();
        springModel.put("walletNumber", "WLT-9988");
        springModel.put("balance", 25400.00);
        springModel.put("owner", "Ramesh Seervi");

        System.out.println("👉 Step 2: Spring 'Model' map populated in memory:");
        System.out.println("   Model Attributes = " + springModel);

        System.out.println("\n👉 Step 3: ViewResolver maps 'wallet-details' to 'templates/wallet-details.html'");
        System.out.println("   Thymeleaf replaces placeholders with Model values:");
        System.out.println("   ┌─────────────────────────────────────────────────────────────┐");
        System.out.println("   │ <h1>FinWallet Dashboard</h1>                                │");
        System.out.println("   │ <p>Owner: " + springModel.get("owner") + "</p>                              │");
        System.out.println("   │ <p>Wallet No: " + springModel.get("walletNumber") + "</p>                           │");
        System.out.println("   │ <p>Available Balance: ₹" + springModel.get("balance") + "</p>                 │");
        System.out.println("   └─────────────────────────────────────────────────────────────┘");
    }

    // =========================================================================
    // 4. Simulation 2: REST API MVC (@RestController + JSON)
    // =========================================================================
    private void simulateRestMvcFlow() {
        System.out.println("\n📌 [PART 3] SIMULATION: MODERN REST MVC FLOW (@RestController + JSON)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Scenario: React/Mobile App calls 'GET /api/wallets/101'");
        System.out.println();
        System.out.println("👉 Step 1: RestController Method Runs:");
        System.out.println("""
               @RestController
               @RequestMapping("/api/wallets")
               public class WalletApiController {
                   @GetMapping("/{id}")
                   public ResponseEntity<WalletResponseDto> getWallet(@PathVariable Long id) {
                       WalletResponseDto dto = new WalletResponseDto("WLT-9988", 25400.00, "ACTIVE");
                       return ResponseEntity.ok(dto); // Direct Java Object!
                   }
               }
            """);

        System.out.println("👉 Step 2: Jackson HttpMessageConverter converts Java POJO -> JSON String:");
        System.out.println("""
               HTTP/1.1 200 OK
               Content-Type: application/json

               {
                   "walletNumber": "WLT-9988",
                   "balance": 25400.00,
                   "status": "ACTIVE",
                   "currency": "INR"
               }
            """);
    }

    // =========================================================================
    // 5. Global Exception Handling: @ControllerAdvice
    // =========================================================================
    private void explainGlobalExceptionHandler() {
        System.out.println("\n📌 [PART 4] MVC BEST PRACTICE: GLOBAL EXCEPTION HANDLING (@ControllerAdvice)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Aapke project ke controllers me try-catch likhne ki zaroorat nahi hoti!");
        System.out.println("Spring MVC me '@RestControllerAdvice' pure application ke exceptions ko ek jagah handle karta hai:\n");
        System.out.println("""
               @RestControllerAdvice
               public class GlobalExceptionHandler {

                   @ExceptionHandler(ResourceNotFoundException.class)
                   public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
                       ErrorResponse error = new ErrorResponse(404, ex.getMessage(), LocalDateTime.now());
                       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                   }

                   @ExceptionHandler(MethodArgumentNotValidException.class)
                   public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
                       // Captures @NotBlank, @Min, @Email validation errors from DTO!
                       Map<String, String> errors = new HashMap<>();
                       ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                       return ResponseEntity.badRequest().body(errors);
                   }
               }
            """);
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println("📂 YOUR REAL MVC CODE IN THIS PROJECT:");
        System.out.println("   - REST Controllers : src/main/java/com/rps/finwallet/controller/");
        System.out.println("   - HTML Controller  : src/main/java/com/rps/finwallet/thymeleaf/ViewController.java");
        System.out.println("   - HTML Templates   : src/main/resources/templates/ (index.html, file-upload.html)");
        System.out.println("════════════════════════════════════════════════════════════════════════════════\n");
    }
}
