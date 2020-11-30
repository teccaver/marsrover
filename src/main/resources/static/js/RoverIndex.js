function initRover() {
    console.log("running here");
    let currentViewedPhoto;
    overrideFormSubmit();
    getAndViewImage();
    initializeModal();
    $('#roverCarousel').on('slid.bs.carousel', function (e) {
        let parentName = '#roverdetails';
        let roverName = $(e.relatedTarget).attr('id');
        console.log("executing slid");
        console.log(e.relatedTarget);
        const Url = 'getRover';
        let id = $(this).attr('id');
        console.log(Url + "  " + id);
        //load rover
        $.get({
            url: Url,
            data: {'roverName': roverName},
            success: function (result) {
                //add it to the page
                // console.log(result);
                let $parent = $(parentName);
                console.log($parent.attr('id'));
                $parent.empty();
                let $el = $(result);
                $parent.append($el);
                reloadForm(roverName);
            },
            error: function (error) {
                console.log('Error retrieving rover ' + roverName);
            }

        })
    });

    function initializeModal() {
        $('#previousImage').on('click', {'direction':'previous'},moveToNextImage);
        $('#nextImage').on('click', {'direction':'next'}, moveToNextImage);
    }

    function getAndViewImage() {

        $("#imageTableData").on("click", function (e) {
            console.log("intercepted image click");
            e.preventDefault();
            let Url = e.target.href;
            currentViewedPhoto = $(e.target).closest('tr');
            viewImage(Url);
            $('#imageViewer').modal('handleUpdate').modal('show');
        })

    }

    function viewImage(url) {
        let currentImage = document.getElementById('currentImage');
        $(currentImage).empty();
        let newImage = new Image();
        newImage.id = 'roverImage';
        newImage.src = url;
        currentImage.appendChild(newImage);
        $('#imageViewer').modal('handleUpdate')
    }

    function moveToNextImage(e) {
        e.preventDefault();
        //find the subsequent row based on button pressed
        let $currentViewedPhoto = $(currentViewedPhoto);
        let data = e.data;
        currentViewedPhoto = data.direction==='next'? $currentViewedPhoto.next('tr') : $currentViewedPhoto.prev('tr');
        let nextImage = $(currentViewedPhoto).find("[data-id='viewablePhoto']");
        viewImage(nextImage.attr('href'));

        if ($(currentViewedPhoto).next('tr').length === 0) {
            //disable the button
            $('#nextImage').attr('disabled', true);
        } else {
            $('#nextImage').attr('disabled', false);
        }
        if ($(currentViewedPhoto).prev('tr').length === 0) {
            //disable the button
            $('#previousImage').attr('disabled', true);
        } else {
            $('#previousImage').attr('disabled', false);
        }
    }

    function populateImageList(imageData) {
        let $table = $('#imageTableData');
        $table.empty();
        let $newData = $(imageData);
        $table.append($newData);
    }


    function resetEarthDatePicker() {
        console.log("picking the date");
        // $('.datepicker').datepicker({
        //     format: 'YYYY-MM-DD',
        //     startDate: '${rover.landingDate}',
        //     endDate: '${rover.maxDate}'
        // })
    }

    function overrideFormSubmit() {
        // var currentRover = $('#roverCarousel.active').attr('id');
        //bounded by landingDate and maxDate
        let maxEarthDate = '${rover.maxEarthDate}';
        let landingDate = '${rover.landingDate}';
        console.log("earth date bounds: " + landingDate + " - " + maxEarthDate);
        resetEarthDatePicker();
        $('#searchForm').on('submit', function (e) {
            //send in the form update the image box
            e.preventDefault();
            console.log("sending get for search form");
            let searchData = $('#searchForm').serializeArray();
            let postData = {};
            //add in any form data that is changed
            let components = Object.values(searchData[0]);
            for (let i = 0; i < components.length; i+= 2) {
                postData[components[i]] = components[i + 1];
            }
            //find all camera that are checked
            postData['cameras'] = [];
            $(e.target).find(':checkbox').each(function () {
                if ($(this).prop('checked')) {
                    //cameras.push($(this).val());
                    postData['cameras'].push($(this).val());
                }
            })

            //get the selected rover
            postData['roverName'] = $('#roverCarousel').find('.active').attr('name');
            let str = JSON.stringify(postData, null, 4);
            console.log(str);
            let Url = 'getImages';
            $.post(Url, postData, function (result) {
                    //if the no result was returned it might just be an empty page refresh
                    //in that case just keep the current content
                    console.log(result);
                    if (result.length > 0) {
                        populateImageList(result);
                    }
                }
            )
        })
    }

    function reloadForm(rover) {
        //send of the request to allow the template engine to reset the form
        let Url = 'reloadSearchForm';
        let formParent = '#formparent'
        console.log("reloading search form");
        $.get({
            url: Url,
            data: {'roverName': rover},
            success: function (result) {
                //replace the form fragment
                let $formParent = $(formParent);
                $formParent.empty();
                let $result = $(result);
                $formParent.append($result);
                overrideFormSubmit();
            },
            error: function (error) {
                console.log('Error reloading search criteria for ' + rover);
            }
        })
    }
}
