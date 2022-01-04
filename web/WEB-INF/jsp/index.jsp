<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en-US" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Welcome</title>
        <meta charset="UTF-8"></meta>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous"/>
    </head>
    <body>
        <div class="container py-5 h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div class="card" style="border-radius: 1rem;">
                        <div class="card-body p-5 text-center">
                    <h2 class="fw-bold mb-2 text-uppercase">Welcome to another Quiz game</h2>
                    <p class="text-black-50 mb-5">Login or register</p>
                    
                    <form method="GET" action="/QuizGame/login">
                        <div class="form-outline form-white mb-4">
                        Username <input type="text" name="username">
                        <label class="form-label"></label>
                        </div>
                        <div class="form-outline form-white mb-4">
                        Password <input type="text" name="password">
                        </div>
                        <div class="d-flex align-items-center justify-content-center pb-4">
                        <button class="btn btn-outline-dark btn-lg px-5" type="submit">Login</button>  
                    </form>

                    <form method="GET" action="/QuizGame/regPage">
                        <button class="btn btn-outline-dark btn-lg px-5" type="submit">Register</button>
                    </form>
                        </div>
                        </div>
                    </div>
                </div>  
            </div>
        </div>
    </body>
</html>
