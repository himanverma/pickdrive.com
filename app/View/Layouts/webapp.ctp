<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Pick Meals</title>
        
        
        
        <?php echo $this->Html->css(array(
            'bootstrap.min',
            'style',
            '/fonts/roboto'
        )); ?>
        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script src="//cdnjs.cloudflare.com/ajax/libs/knockout/3.2.0/knockout-min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/knockout.mapping/2.4.1/knockout.mapping.js"></script>
        <!--        <script src="js/jquery.js"></script>-->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        
        <?php
            echo $this->fetch('startcss');
            $this->Combinator->add_libs('js', array(
                'js/bootstrap.min',
            )); 
            echo $this->Combinator->scripts('js'); // Output Javascript files
            
            echo $this->fetch('startjs');
        ?>
    </head>

    <body>
        <div class="main_container">
            <!--*********************************header***********************************-->
            <header>
                <div class="header_top">
                    <div class="container">
                        <div class="home_sign_in">	
                            <span>
                                <a href="">SIGN IN</a>
                                <a href="">SIGN UP</a>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="header_nav">
                    <nav role="navigation" class="navbar navbar-inverse">
                        <div class="container">
                            <div class="logo">
                                <img src="img/logo.png">
                            </div>
                            <div class="navbar-header">
                                <div aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" class="navbar-toggle collapsed">
                                    <span class="sr-only">Toggle navigation</span>
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>
                                    <span class="icon-bar"></span>
                                </div>
                                <a href="#" class="navbar-brand"></a>
                            </div>
                            <div class="navbar-collapse collapse" id="navbar">
                                <ul class="nav navbar-nav navbar-">
                                    <li><a href="#">HOME</a></li>
                                    <li><a href="#">ABOUT US</a></li>
                                    <li><a href="#">ITEMS</a></li>
                                    <li><a href="#">CONTACT US</a></li>
                                </ul>
                                <form class="navbar-form navbar-right">
                                    <input type="text" placeholder="Search..." class="form-control">
                                </form>
                            </div>
                        </div>
                    </nav>
                </div>
            </header>
            
            <?php echo $this->fetch('content'); ?>

            <footer>
                <div class="container">
                    <div class="footer_in">
                        <p>Copyright © 2013 by Company Name</p>
                    </div>
                </div>
            </footer>

            <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        </div>
        <script type="text/javascript">


            var CombinationVM = function() {
                var me = this;
                me.display_name = ko.observable();
                me.url = ko.observable();
                me.name = ko.observable();
                me.price = ko.observable();
                me.SearchMeal = ko.observable();
                me.Combolist = ko.observableArray([]);
                me.cartItems = ko.observableArray([]);
                me.getData = function() {
                    var m = me;
                    $.post('http://uneekarts.com/office/ofood/api/combinations.json', {
                        "data[User][latitude]": 30.7238504,
                        "data[User][longitude]": 76.8465098,
                        "data[User][count]": 4
                    }, function(d) {
                        m.Combolist(d.data.items);
                    });
                };
                me.getData();
                me.Search = function() {
                    $.post('http://uneekarts.com/office/ofood/api/combinations/search.json', {
                        "data[Combination][search]": me.SearchMeal(),
                    }, function(d) {
                        console.log(d);
                        me.Combolist(d.data);
                    });
                };
                me.addToCart = function(d, e) {
                    CartObj.pushToCart(d, 1, d.Combination.price);

                };
            };

            var CartVM = function() {
                var me = this;
                me.items = ko.observableArray([]);
                me.subt = ko.computed(function() {
                    var x = 0;
                    var d = this.items();
                    for (i in d) {
                        x += d[i].qty() * d[i].price();
                    }
                    return x;
                }, this);

                me.pushToCart = function(item, qty, price) {
                    me.items.push({
                        data: item,
                        qty: ko.observable(qty),
                        price: ko.observable(price)
                    });
                    //var x = me.
                    //();
                    //me.subt((qty*price) + x );
                }
                me.increase = function(d, e) {
                    d.qty(d.qty() + 1);
                };
                me.decrease = function(d, e) {
                    if (d.qty() == "1") {
                        me.items.remove(d);
                    } else {
                        d.qty(d.qty() - 1);
                    }
                }
                me.subTotal = ko.computed(function() {
                    return 0;
                }, this);
                me.serviceTax = ko.computed(function() {
                    return 0;
                }, this);
                me.vat = ko.computed(function() {
                    return 0;
                }, this);
                me.total = ko.computed(function() {
                    return this.subt();
                }, this);
            };


            CartObj = new CartVM();
            ComboObj = new CombinationVM();

            $(document).ready(function() {
                ko.applyBindings(ComboObj, $('#combination-sec')[0]);
                ko.applyBindings(CartObj, $('#cart-sec')[0]);
            });
        </script>
        <script type="text/javascript">
            $(function() {
                var offset = $("#sidebar").offset();
                var topPadding = 15;
                $(window).scroll(function() {
                    if ($(window).scrollTop() > offset.top) {
                        $("#sidebar").stop().animate({
                            marginTop: $(window).scrollTop() - offset.top + topPadding
                        });
                    } else {
                        $("#sidebar").stop().animate({
                            marginTop: 0
                        });
                    }
                    ;
                });
            });
        </script>
    </body>
</html>