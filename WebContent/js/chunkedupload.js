/**
 * 
 */
$(function () {
    $('#fileupload').fileupload({
    	maxChunkSize: 10000, // 10 KB
    	dataType:'json',
    	add:function(e, data){
    		var that = this; //?
    		$.getJSON('http://192.168.2.199:8080/resumepoint', {file: data.files[0].name}, function(result){ // 给指定url发送文件名称
    			var file = result.file;
    			data.uploadedBytes = file && file.size; // ?
    			$.blueimp.fileupload.prototype.options.add.call(that, e, data); // ?
    		});	
    	},
    	drop: function (e, data) { // drop事件，jquery的fileupload插件默认支持拖动上传，当该事件被触发时，会调用我们自定义回调函数
            $.each(data.files, function (index, file) {
                console.info('Dropped file: ' + file.name);
            });
        },
        change: function (e, data) {// change事件，当文件输入框发生改变时，触发文件上传, 触发时间早于add
            $.each(data.files, function (index, file) {
                console.info('Selected file: name=' + file.name + '\tsize=' + file.size + '\tmozFullPath=' + file.mozFullPath + '\ttype=' + file.type);
            });
        },
        done: function (e, data) { // done事件，文件上传完成后调用
            $.each(data.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            });
        }
    }).on('fileuploadchunksend', function (e, data) {
    	console.info('fileuploadchunksend');
    })
    .on('fileuploadchunkdone', function (e, data) {
    	console.info('fileuploadchunkdone');
    })
    .on('fileuploadchunkfail', function (e, data) {
    	console.info('fileuploadchunkfail');
    })
    .on('fileuploadchunkalways', function (e, data) {
    	console.info('fileuploadchunkalways');
    });
});

