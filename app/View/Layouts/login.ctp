<!DOCTYPE html>
<html class="bg-black">
    <head>
        <?php echo $this->Html->charset(); ?>
        <title>RugBuilder | <?php echo @$title_for_layout; ?></title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <?php 
            echo $this->Html->css(array(
                'admin/bootstrap.min',
                'admin/font-awesome.min',
                'admin/ionicons.min',
                'admin/AdminLTE'
            ));
        ?>
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
    </head>
    <body class="bg-black">

        <div class="form-box" id="login-box">
            <div class="header">Sign In</div>
            <div style="text-align: center;"><?php echo $this->Session->flash(); ?></div>
            <form method="post">
                <div class="body bg-gray">
                    <div class="form-group">
                        <input type="text" name="data[User][username]" class="form-control" value="admin" placeholder="User ID"/>
                    </div>
                    <div class="form-group">
                        <input type="password" name="data[User][password]" class="form-control" value="admin@123" placeholder="Password"/>
                    </div>
                    <div class="form-group">
                        <select name="data[User][type]" class="form-control">
                            <option value="vendor">Chef (Food Supplier)</option>
                            <option value="customer">Customer</option>
                            <option selected="" value="admin">Administrator</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <input type="checkbox" name="remember_me"/> Remember me
                    </div>
                </div>
                <div class="footer">                                                               
                    <button type="submit" class="btn bg-olive btn-block">Sign me in</button>  
                    
                    <p><a href="#">I forgot my password</a></p>
                    
                    <!--<a href="register.html" class="text-center">Register a new membership</a>-->
                </div>
            </form>

            <!--<div class="margin text-center">
                <span>Sign in using social networks</span>
                <br/>
                <button class="btn bg-light-blue btn-circle"><i class="fa fa-facebook"></i></button>
                <button class="btn bg-aqua btn-circle"><i class="fa fa-twitter"></i></button>
                <button class="btn bg-red btn-circle"><i class="fa fa-google-plus"></i></button>

            </div>-->
        </div>


        <!-- jQuery 2.0.2 -->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
        <!-- Bootstrap -->
        <?php echo $this->Html->script(array('admin/bootstrap.min')); ?>
        

    </body>
</html>