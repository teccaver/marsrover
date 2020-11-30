function initRover() {
    let currentViewedPhoto;
    let postData;
    overrideFormSubmit();
    getAndViewImage();
    initializeModal();
    $('#roverCarousel').on('slid.bs.carousel', function (e) {
        let parentName = '#roverdetails';
        let roverName = $(e.relatedTarget).attr('id');
        const Url = 'getRover';
        //load rover
        $.get({
            url: Url,
            data: {'roverName': roverName},
            success: function (result) {
                //add it to the page
                let $parent = $(parentName);
                $parent.empty();
                let $el = $(result);
                $parent.appendChild($el);
                reloadForm(roverName);
            },
            error: function (error) {
                console.log('Error retrieving rover ' + roverName);
            }

        })
    });

    function initializeModal() {
        $('#previousImage').on('click', {'direction': 'previous'}, moveToNextImage);
        $('#nextImage').on('click', {'direction': 'next'}, moveToNextImage);
        $('#imageViewer').on('hidden.bs.modal',function(){
            //repopulate the form to pick up new cache values
            populateImageListFromPostData();
            return true;
        })
    }

    function getAndViewImage() {

        $("#imageTableData").on("click", function (e) {
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
        currentViewedPhoto = data.direction === 'next' ? $currentViewedPhoto.next('tr') : $currentViewedPhoto.prev('tr');
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

    function populateImageListFromPostData(){
        let Url = 'getImages';
        $.post(Url, postData, function (result) {
            //if the no result was returned it might just be an empty page refresh
            //in that case just keep the current content
            // console.log(result);
            if (result.length > 0) {
                populateImageList(result);
            }
        })
    }
    function populateImageList(imageData) {
        let $table = $('#imageTableData');
        $table.empty();
        let $newData = $(imageData);
        $table.append($newData);
    }

    function overrideFormSubmit() {
        // var currentRover = $('#roverCarousel.active').attr('id');
        //bounded by landingDate and maxDate
        $('#searchForm').on('submit', function (e) {
            //send in the form update the image box
            e.preventDefault();
            let searchData = $('#searchForm').serializeArray();
            postData = {};
            //add in any form data that is changed
            for (let i = 0; i < searchData.length; i++) {
                let components = Object.values(searchData[i]);
                postData[components[0]] = components[1];
            }
            //get the selected rover
            postData['roverName'] = $('#roverCarousel').find('.active').attr('name');
            let earthDate = postData['earthDateStart']
            if (earthDate) {
                if (!verifyEarthDate(earthDate)) {
                    alert('Date is invalid. Date format is YYYY-MM-DD and is bounded by landing and max earth date');
                }
            }
            //find all camera that are checked
            postData['cameras'] = [];
            $(e.target).find(':checkbox').each(function () {
                if ($(this).prop('checked')) {
                    postData['cameras'].push($(this).val());
                }
            })

            // let str = JSON.stringify(postData, null, 4);
            // console.log(str);
            populateImageListFromPostData();
        })
    }

    function verifyEarthDate(earthDate) {
        //do ajax to get the rover details to verify the bounds of earth date
        //or pull the info from the rover detail page
        //for now just check the format

        let pattern = /([\d]{4})-([\d]{2})-([\d]{2})/;
        let match = earthDate.match(pattern);
        let valid = false;
        let groupYear = 1;
        let groupMonth = 2;
        let groupDay = 3;
        if (match.length === 4) {
            let minDate = (document.getElementById('minEarth').innerText);
            let maxDate = (document.getElementById('maxEarth').innerText);
            let year = match[groupYear];
            if (year) {
                let month = match[groupMonth];
                if (month) {
                    //convert to number and verify bounds
                    let day = match[groupDay];
                    if (day) {
                        //format of entered date is correct. check bounds
                        let minD = new Date(minDate).getTime();
                        let maxD = new Date(maxDate).getTime();
                        let date = new Date(earthDate).getTime();
                        if (minD <= date && date <= maxD) {
                            valid = true;
                        }
                    }
                }
            }
        }
        return valid;
    }

    function reloadForm(rover) {
        //send of the request to allow the template engine to reset the form
        let Url = 'reloadSearchForm';
        let formParent = '#formparent'
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
