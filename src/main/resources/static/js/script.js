$(document).ready(function () {
    var multipleCardCarousel = document.querySelector(
        "#cardCarousel"
    );
    if (window.matchMedia("(min-width: 576px)").matches) {
        var carousel = new bootstrap.Carousel(multipleCardCarousel, {
            interval: false,
            wrap: false
        });
        var carouselWidth = $("#cardCarousel .carousel-inner")[0].scrollWidth;
        var cardWidth = $("#cardCarousel .carousel-item").width();
        var scrollPosition = 0;
        let visibleCards = 3;
        $("#cardCarousel .carousel-control-next").on("click", function () {
            if (scrollPosition < carouselWidth - cardWidth * visibleCards) {
                scrollPosition += cardWidth;
                $("#cardCarousel .carousel-inner").animate(
                    { scrollLeft: scrollPosition },
                    600
                );
            }
        });
        $("#cardCarousel .carousel-control-prev").on("click", function () {
            if (scrollPosition > 0) {
                scrollPosition -= cardWidth;
                $("#cardCarousel .carousel-inner").animate(
                    { scrollLeft: scrollPosition },
                    600
                );
            }
        });
    } else {
        $(multipleCardCarousel).addClass("slide");
    }
});