<?php
# contact: gajok@cs.washington.edu
# Front Page

include("common.php");

top();

?>
<!--		<div id="signup">
			<a href="login.php"> login </a> <br />
			New Here? <a href="register.php"> Sign Up </a>
		</div>
-->
		<div class="user">
			<ul>				
				<li>
					<a href="type_menu.php">
						<img src="http://students.washington.edu/dongm/454/img/menu.gif" alt="icon" />
						Find Restaurants By Menues !!!
					</a>
				</li>
				
				<li>
					<a href="add_food.php">
						<img src="http://students.washington.edu/dongm/454/img/signup.gif" alt="icon" />
						Add Good Menues You Know !!!
					</a>
				</li>
			</ul>
		</div>

<?php item_list(); ?>		

		<div id="copyright">
			<p>
				Results and page (C) Copyright 2012 SFSP.
			</p>
		</div>
	</body>
</html>
