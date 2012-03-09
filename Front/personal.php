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
		<div class="user">
			<ul>				
				<li>
					<a href="type_menu.php">
						<img src="http://students.washington.edu/dongm/454/img/menu.gif" alt="icon" />
						Find Restaurants By Menues !!!
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
