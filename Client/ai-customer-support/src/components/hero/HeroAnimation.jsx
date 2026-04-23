import React from "react";
import Slider from "react-slick";
import s1 from "../../assets/images/support1.jpg";
import s2 from "../../assets/images/support2.jpg";
import s3 from "../../assets/images/support3.jpg";
import s4 from "../../assets/images/support4.jpg";
import s5 from "../../assets/images/support5.jpg";

const images = [s1, s2, s3, s4, s5];

// VURUCU HAMLE BURADA: Eğer Slider bir obje olarak (Slider.default) geldiyse onu al,
// normal geldiyse kendisini kullan. (Vite uyumluluk hilesi)
const Slick = Slider.default ? Slider.default : Slider;

const HeroAnimation = () => {
  const settings = {
    dots: true,
    infinite: true,
    speed: 15000, // Not: Bu 15 saniye sürer, çok yavaş olabilir. 1500 (1.5 sn) yapmanı tavsiye ederim.
    fade: true,
    autoplay: true,
    autoplaySpeed: 3000,
  };

  return (
    <div className="slider-container">
      <Slick {...settings}>
        {images.map((img, index) => (
          <div key={index}>
            <img src={img} alt={`Slide ${index}`} className="slider-image" />
          </div>
        ))}
      </Slick>
    </div>
  );
};

export default HeroAnimation;
