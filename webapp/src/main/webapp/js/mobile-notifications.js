
$(".checkbox").on("click", function(e) {
	$('#savingStatus').text("Saving...").removeClass("alert-success alert-warning").addClass("alert-info");
	$(this).jzAjax("UserSettings.saveSetting()", {
		data : {
			"param" : $(this).attr("id")
		},
		dataType:"text",
		success: function(data) {
			$('#savingStatus').text(data).removeClass("alert-info").addClass("alert-success");
		}
	}).fail(function(jqXHR, data) {
			$('#savingStatus').text(data).removeClass("alert-info").addClass("alert-warning");
	});
});
