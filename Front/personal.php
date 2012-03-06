<?php
# Donghyo Min
# contact: gajok@cs.washington.edu
# filename: personal.php

include("common.php");
session_start();
top();
if (!$_SESSION["valid_user"]) {
	# redirect to login page.
	Header("Location: login.php");
} 

?>
<!--
		<div id="signup">
			<a href="logout.php"> logout!</a>
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
